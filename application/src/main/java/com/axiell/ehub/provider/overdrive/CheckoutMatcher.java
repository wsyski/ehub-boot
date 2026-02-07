package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.util.IMatcher;

class CheckoutMatcher implements IMatcher<CheckoutDTO> {
    private final String productId;

    CheckoutMatcher(final String productId) {
        this.productId = productId;
    }

    @Override
    public boolean matches(CheckoutDTO checkout) {
        final String reserveId = checkout.getReserveId();
        return productId.equals(reserveId);
    }
}
