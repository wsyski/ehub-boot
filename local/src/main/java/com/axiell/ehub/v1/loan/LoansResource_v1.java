/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.security.AuthInfo;

public final class LoansResource_v1 implements ILoansResource_v1 {
    private ILoanBusinessController loanBusinessController;

    @Override
    public ReadyLoan_v1 createLoan(AuthInfo authInfo, String language, PendingLoan_v1 pendingLoan_v1) {
        PendingLoan pendingLoan = PendingLoanV1Converter.convert(pendingLoan_v1);
        ReadyLoan readyLoan = loanBusinessController.createLoan(authInfo, pendingLoan, language);
        // TODO
//        return ReadyLoanV1Converter.convert(readyLoan);
        return null;
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, Long readyLoanId, String language) {
        ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, readyLoanId, language);
        // TODO
//        return ReadyLoanV1Converter.convert(readyLoan);
        return null;
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, String lmsLoanId, String language) {
        CheckoutsSearchResult checkoutsSearchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        CheckoutMetadata checkoutMetadata = checkoutsSearchResult.findCheckoutByLmsLoanId(lmsLoanId);
        // TODO:
        Checkout checkout = null;
        return ReadyLoanV1Converter.convert(checkout);
    }

    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }
}
