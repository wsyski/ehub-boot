package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.format.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Elib3DataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IElib3CommandChainFactory commandChainFactory;

    @Override
    public Formats getFormats(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String contentProviderRecordId, final String language) {
        final Elib3CommandData data = Elib3CommandData.newInstance(contentProviderConsumer, libraryCard, contentProviderRecordId, language);
        final GetFormatsCommandChain commandChain = commandChainFactory.createGetFormatsCommandChain();
        return commandChain.execute(data);
    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin, final PendingLoan pendingLoan, final String language) {
        final CommandData data = CommandData.newInstance(contentProviderConsumer, libraryCard).setPendingLoan(pendingLoan).setLanguage(language);
        final CreateLoanCommandChain commandChain = commandChainFactory.createCreateLoanCommandChain();
        return commandChain.execute(data);
    }

    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin, final ContentProviderLoanMetadata contentProviderLoanMetadata, final String language) {
        final CommandData data = CommandData.newInstance(contentProviderConsumer, libraryCard).setContentProviderLoanMetadata(contentProviderLoanMetadata).setLanguage(language);
        final GetContentCommandChain commandChain = commandChainFactory.createGetContentCommandChain();
        return commandChain.execute(data);
    }
}
