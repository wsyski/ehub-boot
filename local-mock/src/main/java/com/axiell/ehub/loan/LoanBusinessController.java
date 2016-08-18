package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.security.AuthInfo;

public class LoanBusinessController implements ILoanBusinessController {

    @Override
    public CheckoutsSearchResult search(final AuthInfo authInfo, final String lmsLoanId, final String language) {
        return null;
    }

    @Override
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        return null;
    }

    @Override
    public Checkout getCheckout(final AuthInfo authInfo, final long readyLoanId, final String language) {
        return null;
    }
}
