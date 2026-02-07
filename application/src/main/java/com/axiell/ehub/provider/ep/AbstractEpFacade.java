package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.authinfo.Patron;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEpFacade<C extends ICheckoutDTO, R extends ICheckoutRequestDTO, I extends IEpResource<C, R>> implements IEpFacade<C> {
    @Autowired
    private EpResourceFactory epResourceFactory;

    @Override
    public RecordDTO getRecord(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final I epResource = getEpResource(contentProviderConsumer, patron);
        return epResource.getRecord(contentProviderRecordId);
    }

    @Override
    public C getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderLoanId) {
        final I epResource = getEpResource(contentProviderConsumer, patron);
        return epResource.getCheckout(contentProviderLoanId);
    }

    @Override
    public void deleteCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                               final String contentProviderLoanId) {
        final I epResource = getEpResource(contentProviderConsumer, patron);
        epResource.deleteCheckout(contentProviderLoanId);
    }

    protected I getEpResource(final ContentProviderConsumer contentProviderConsumer, final Patron patron) {
        return epResourceFactory.create(getIEpResourceClass(), contentProviderConsumer, patron);
    }

    protected abstract Class<I> getIEpResourceClass();
}


