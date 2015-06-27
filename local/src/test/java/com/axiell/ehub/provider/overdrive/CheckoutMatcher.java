package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.util.IMatcher;

class CheckoutMatcher implements IMatcher<Checkout> {
    private final String productId;

    CheckoutMatcher(final String productId) {
        this.productId = productId;
    }

    @Override
    public boolean matches(Checkout checkout) {
        final String reserveId = checkout.getReserveId();
        return productId.equals(reserveId);
    }
}
