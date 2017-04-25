package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class BorrowBoxFacade implements IBorrowBoxFacade {
    @Autowired
    private BorrowBoxResourceFactory borrowBoxResourceFactory;

    @Override
    public FormatsDTO getFormats(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                 final String contentProviderRecordId) {
        final IBorrowBoxResource borrowBoxResource = borrowBoxResourceFactory.create(contentProviderConsumer, patron, language);
        return borrowBoxResource.getFormats(contentProviderRecordId);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                final String contentProviderRecordId,
                                final String formatId) {
        final IBorrowBoxResource borrowBoxResource = borrowBoxResourceFactory.create(contentProviderConsumer, patron, language);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return borrowBoxResource.checkout(checkoutRequest);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String language,
                                   final String contentProviderLoanId) {
        final IBorrowBoxResource borrowBoxResource = borrowBoxResourceFactory.create(contentProviderConsumer, patron, language);
        return borrowBoxResource.getCheckout(contentProviderLoanId);
    }
}
