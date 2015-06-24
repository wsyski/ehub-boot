package com.axiell.ehub.provider;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import com.axiell.ehub.provider.record.format.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.util.EhubCharsets.UTF_8;

@Component
public class ContentProviderDataAccessorFacade implements IContentProviderDataAccessorFacade {
    @Autowired
    private IAliasBusinessController aliasBusinessController;
    @Autowired
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;

    @Override
    public Formats getFormats(EhubConsumer ehubConsumer, String contentProviderAlias, Patron patron, String contentProviderRecordId, String language) {
        final ContentProviderName name = aliasBusinessController.getName(contentProviderAlias);
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        final CommandData commandData = CommandData.newInstance(consumer, patron, language).setContentProviderRecordId(contentProviderRecordId)
                .setContentProviderAlias(contentProviderAlias);
        return dataAccessor.getFormats(commandData);
    }

    @Override
    public ContentProviderLoan createLoan(EhubConsumer ehubConsumer, Patron patron, PendingLoan pendingLoan, String language) {
        final String contentProviderAlias = pendingLoan.contentProviderAlias();
        final ContentProviderName name = aliasBusinessController.getName(contentProviderAlias);
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        final CommandData commandData = CommandData.newInstance(consumer, patron, language).setPendingLoan(pendingLoan);
        return dataAccessor.createLoan(commandData);
    }

    @Override
    public ContentLink getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, Patron patron, String language) {
        final ContentProviderLoanMetadata metadata = ehubLoan.getContentProviderLoanMetadata();
        final ContentProviderName name = getContentProviderName(metadata);
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        final CommandData commandData = CommandData.newInstance(consumer, patron, language).setContentProviderLoanMetadata(metadata);
        return dataAccessor.getContent(commandData);
    }

    private ContentProviderName getContentProviderName(final ContentProviderLoanMetadata metadata) {
        final ContentProvider contentProvider = metadata.getContentProvider();
        return contentProvider.getName();
    }
}
