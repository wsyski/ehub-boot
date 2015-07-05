package com.axiell.ehub.provider.ep;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

@Component
class EpFacade implements IEpFacade {

    @Override
    public FormatsDTO getFormats(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final IEpResource providerResource = EpResourceFactory.create(contentProviderConsumer, patron);
        return providerResource.getFormats(contentProviderRecordId);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                final String formatId) {
        final IEpResource borrowBoxResource = EpResourceFactory.create(contentProviderConsumer, patron);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return borrowBoxResource.checkout(checkoutRequest);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron,
                                   final String contentProviderLoanId) {
        final IEpResource borrowBoxResource = EpResourceFactory.create(contentProviderConsumer, patron);
        return borrowBoxResource.getCheckout(contentProviderLoanId);
    }
}
