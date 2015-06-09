package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.patron.Patron;
import org.springframework.stereotype.Component;

@Component
class BorrowBoxFacade implements IBorrowBoxFacade {

    @Override
    public FormatsDTO getFormats(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId) {
        final AuthenticationToken authenticationToken = new AuthenticationToken(contentProviderConsumer, patron);
        final IBorrowBoxResource borrowBoxResource = BorrowBoxResourceFactory.create(contentProviderConsumer);
        return borrowBoxResource
                .getFormats(authenticationToken, authenticationToken.getLibraryId(), authenticationToken.getLibraryCard(), contentProviderRecordId);
    }

    @Override
    public CheckoutDTO checkout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderRecordId,
                                final String formatId) {
        final AuthenticationToken authenticationToken = new AuthenticationToken(contentProviderConsumer, patron);
        final IBorrowBoxResource borrowBoxResource = BorrowBoxResourceFactory.create(contentProviderConsumer);
        final CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(contentProviderRecordId, formatId);
        return borrowBoxResource.checkout(authenticationToken, authenticationToken.getLibraryId(), authenticationToken.getLibraryCard(), checkoutRequest);
    }

    @Override
    public CheckoutDTO getCheckout(final ContentProviderConsumer contentProviderConsumer, final Patron patron, final String contentProviderLoanId) {
        final AuthenticationToken authenticationToken = new AuthenticationToken(contentProviderConsumer, patron);
        final IBorrowBoxResource borrowBoxResource = BorrowBoxResourceFactory.create(contentProviderConsumer);
        return borrowBoxResource
                .getCheckout(authenticationToken, authenticationToken.getLibraryId(), authenticationToken.getLibraryCard(), contentProviderLoanId);
    }
}
