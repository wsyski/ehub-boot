/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.authinfo.AuthInfo;

/**
 * This interface should only be used by the resources of the eHUB REST interface or by other business controllers.
 */
public interface ILoanBusinessController {

    CheckoutsSearchResult search(AuthInfo authInfo, String lmsLoanId, String language);

    Checkout checkout(AuthInfo authInfo, Fields fields, String language);

    Checkout getCheckout(AuthInfo authInfo, long readyLoanId, String language);
}
