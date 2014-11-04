package com.axiell.ehub.provider.ocd;

import java.util.List;

class CheckoutFinder {

    private CheckoutFinder() {
    }

    static CheckoutDTO find(ICheckoutMatcher matcher, List<CheckoutDTO> checkouts) throws CheckoutNotFoundException {
        for (CheckoutDTO checkout : checkouts) {
            if (matcher.matches(checkout))
                return checkout;
        }
        throw new CheckoutNotFoundException();
    }
}
