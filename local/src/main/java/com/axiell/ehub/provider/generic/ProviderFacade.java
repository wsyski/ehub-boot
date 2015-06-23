package com.axiell.ehub.provider.generic;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

@Component
class ProviderFacade implements IProviderFacade {

    @Override
    public FormatsDTO getFormats(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                 final String contentProviderRecordId) {
        final IProviderResource providerResource = ProviderResourceFactory.create(contentProviderConsumer, patron);
        return providerResource.getFormats(contentProviderRecordId, language);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                final String contentProviderRecordId,
                                final String formatId) {
        final IProviderResource borrowBoxResource = ProviderResourceFactory.create(contentProviderConsumer, patron);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return borrowBoxResource.checkout(checkoutRequest, language);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                   final String contentProviderLoanId) {
        final IProviderResource borrowBoxResource = ProviderResourceFactory.create(contentProviderConsumer, patron);
        return borrowBoxResource.getCheckout(contentProviderLoanId, language);
    }
}
