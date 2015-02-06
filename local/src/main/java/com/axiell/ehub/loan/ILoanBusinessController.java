/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

/**
 * Defines all business methods related to PendingLoan}s and ReadyLoans.
 * <p/>
 * <p>This interface should only be used by the resources of the eHUB REST interface or by other business controllers.</p>
 */
public interface ILoanBusinessController {

    CheckoutsSearchResult search(AuthInfo authInfo, String lmsLoanId, String language);

    Checkout checkout(AuthInfo authInfo, PendingLoan pendingLoan, String language);

    Checkout getCheckout(AuthInfo authInfo, Long readyLoanId, String language);
}
