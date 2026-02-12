package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.util.IMatcher;

public class CheckoutMatcher implements IMatcher<CheckoutDTO> {
    private final String productId;

    public CheckoutMatcher(final String productId) {
        this.productId = productId;
    }

    @Override
    public boolean matches(CheckoutDTO checkout) {
        final String reserveId = checkout.getReserveId();
        return productId.equals(reserveId);
    }
}
