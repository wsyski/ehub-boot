package com.axiell.ehub.provider;

import com.axiell.ehub.provider.routing.IRoutingBusinessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.EhubLoan;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.Formats;

@Component
public class ContentProviderDataAccessorFacade implements IContentProviderDataAccessorFacade {
    @Autowired
    private IRoutingBusinessController routingBusinessController;
    @Autowired
    private IContentProviderDataAccessorFactory contentProviderDataAccessorFactory;

    @Override
    public Formats getFormats(EhubConsumer ehubConsumer, String contentProviderName, String libraryCard, String contentProviderRecordId, String language) {
        final ContentProviderName name = routingBusinessController.getTarget(contentProviderName);
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        return dataAccessor.getFormats(consumer, libraryCard, contentProviderRecordId, language);
    }

    @Override
    public ContentProviderLoan createLoan(EhubConsumer ehubConsumer, String libraryCard, String pin, PendingLoan pendingLoan, String language) {
        final ContentProviderName name = routingBusinessController.getTarget(pendingLoan.getContentProviderName());
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        return dataAccessor.createLoan(consumer, libraryCard, pin, pendingLoan, language);
    }

    @Override
    public IContent getContent(EhubConsumer ehubConsumer, EhubLoan ehubLoan, String libraryCard, String pin, String language) {
        final ContentProviderLoanMetadata metadata = ehubLoan.getContentProviderLoanMetadata();
        final ContentProviderName name = getContentProviderName(metadata);
        final ContentProviderConsumer consumer = ehubConsumer.getContentProviderConsumer(name);
        final IContentProviderDataAccessor dataAccessor = contentProviderDataAccessorFactory.getInstance(name);
        return dataAccessor.getContent(consumer, libraryCard, pin, metadata, language);
    }

    private ContentProviderName getContentProviderName(ContentProviderLoanMetadata metadata) {
        final ContentProvider contentProvider = metadata.getContentProvider();
        return contentProvider.getName();
    }
}
