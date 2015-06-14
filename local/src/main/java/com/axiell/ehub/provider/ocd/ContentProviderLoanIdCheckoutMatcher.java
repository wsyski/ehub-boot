package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.IMatcher;

class ContentProviderLoanIdCheckoutMatcher implements IMatcher<CheckoutDTO> {
    private final String contentProviderLoanId;

    ContentProviderLoanIdCheckoutMatcher(String contentProviderLoanId) {
        this.contentProviderLoanId = contentProviderLoanId;
    }

    ContentProviderLoanIdCheckoutMatcher(Checkout checkout) {
        this.contentProviderLoanId = checkout.getTransactionId();
    }

    @Override
    public boolean matches(CheckoutDTO checkoutDTO) {
        final String transactionId = checkoutDTO.getTransactionId();
        return contentProviderLoanId.equals(transactionId);
    }
}
