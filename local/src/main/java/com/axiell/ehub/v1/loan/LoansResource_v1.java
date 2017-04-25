/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.loan.*;
import com.axiell.auth.AuthInfo;

public final class LoansResource_v1 implements ILoansResource_v1 {
    private ILoanBusinessController loanBusinessController;

    @Override
    public ReadyLoan_v1 createLoan(AuthInfo authInfo, String language, PendingLoan_v1 pendingLoan_v1) {
        Fields fields = PendingLoanConverter_v1.convert(pendingLoan_v1);
        Checkout checkout = loanBusinessController.checkout(authInfo, fields, language);
        return ReadyLoanConverter_v1.convert(checkout);
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, Long readyLoanId, String language) {
        Checkout checkout = loanBusinessController.getCheckout(authInfo, readyLoanId, language);
        return ReadyLoanConverter_v1.convert(checkout);
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, String lmsLoanId, String language) {
        CheckoutsSearchResult checkoutsSearchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        CheckoutMetadata checkoutMetadata = checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
        Checkout checkout = loanBusinessController.getCheckout(authInfo, checkoutMetadata.getId(), language);
        return ReadyLoanConverter_v1.convert(checkout);
    }

    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }
}
