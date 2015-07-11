package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

@Component
class EpFacade implements IEpFacade {

    @Override
    public RecordDTO getRecord(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final IEpResource epResource = EpResourceFactory.create(contentProviderConsumer, patron);
        return epResource.getRecord(contentProviderRecordId);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                final String formatId) {
        final IEpResource epResource = EpResourceFactory.create(contentProviderConsumer, patron);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return epResource.checkout(checkoutRequest);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                                   final String contentProviderLoanId) {
        final IEpResource epResource = EpResourceFactory.create(contentProviderConsumer, patron);
        return epResource.getCheckout(contentProviderLoanId);
    }
}
