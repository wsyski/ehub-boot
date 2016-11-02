package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.CommandData;

interface IOcdCheckoutHandler {

    Checkout checkout(CommandData data);

    Checkout getCheckout(CommandData data, String contentProviderLoanId);
}
