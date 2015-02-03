/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.security.AuthInfo;

public interface IEhubService {

    CheckoutMetadata findCheckoutByLmsLoanId(AuthInfo authInfo, String lmsLoanId, String language);

    Checkout getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language);

    Checkout checkout(AuthInfo authInfo, Fields fields, String language);
}
