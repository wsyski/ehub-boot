package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.CommandData;

interface IOcdCheckoutHandler {

    Checkout checkout(BearerToken bearerToken, CommandData data);

    Checkout getCompleteCheckout(BearerToken bearerToken, CommandData data, String contentProviderLoanId);
}
