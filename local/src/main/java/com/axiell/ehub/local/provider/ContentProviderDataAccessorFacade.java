package com.axiell.ehub.local.provider;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.loan.EhubLoan;
import com.axiell.ehub.local.loan.PendingLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentProviderDataAccessorFacade implements IContentProviderDataAccessorFacade {
    @Autowired
    private IAliasBusinessController aliasBusinessController;
    @Autowired
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;

    @Override
    public List<Issue> getIssues(final EhubConsumer ehubConsumer, final String contentProviderAlias, final Patron patron, final String contentProviderRecordId,
                                 final String language) {
        final ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(ehubConsumer, contentProviderAlias);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(contentProvider);
        final CommandData commandData = CommandData.newInstance(contentProviderConsumer, patron, language).setContentProviderRecordId(contentProviderRecordId)
                .setContentProviderAlias(contentProviderAlias);
        return dataAccessor.getIssues(commandData);
    }

    @Override
    public ContentProviderLoan createLoan(final EhubConsumer ehubConsumer, final Patron patron, final PendingLoan pendingLoan, final String language) {
        final String contentProviderAlias = pendingLoan.contentProviderAlias();
        final ContentProviderConsumer contentProviderConsumer = getContentProviderConsumer(ehubConsumer, contentProviderAlias);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(contentProvider);
        final CommandData commandData = CommandData.newInstance(contentProviderConsumer, patron, language).setPendingLoan(pendingLoan);
        return dataAccessor.createLoan(commandData);
    }

    @Override
    public Content getContent(final EhubConsumer ehubConsumer, final EhubLoan ehubLoan, final FormatDecoration formatDecoration, final Patron patron,
                              final String language) {
        final ContentProviderLoanMetadata metadata = ehubLoan.getContentProviderLoanMetadata();
        final ContentProvider contentProvider = metadata.getContentProvider();
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(contentProvider.getName());
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(contentProvider);
        final CommandData commandData =
                CommandData.newInstance(consumer, patron, language).setContentProviderLoanMetadata(metadata).setFormatDecoration(formatDecoration);
        return dataAccessor.getContent(commandData);
    }

    @Override
    public ContentProviderConsumer getContentProviderConsumer(final EhubConsumer ehubConsumer, final String contentProviderAlias) {
        final String name = aliasBusinessController.getName(contentProviderAlias);
        return ehubConsumer.getContentProviderConsumer(name);
    }
}
