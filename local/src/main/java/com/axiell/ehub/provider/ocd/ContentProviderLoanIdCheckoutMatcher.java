package com.axiell.ehub.provider.ocd;

class ContentProviderLoanIdCheckoutMatcher implements ICheckoutMatcher {
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
