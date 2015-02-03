/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.ehub.loan.*;
import com.axiell.ehub.security.AuthInfo;

public final class LoansResource_v1 implements ILoansResource_v1 {
    private ILoanBusinessController loanBusinessController;

    @Override
    public ReadyLoan_v1 createLoan(AuthInfo authInfo, String language, PendingLoan_v1 pendingLoan_v1) {
        PendingLoan pendingLoan = PendingLoanV1Converter.convert(pendingLoan_v1);
        ReadyLoan readyLoan = loanBusinessController.createLoan(authInfo, pendingLoan, language);
        return ReadyLoanV1Converter.convert(readyLoan);
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, Long readyLoanId, String language) {
        ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, readyLoanId, language);
        return ReadyLoanV1Converter.convert(readyLoan);
    }

    @Override
    public ReadyLoan_v1 getLoan(AuthInfo authInfo, String lmsLoanId, String language) {
        ReadyLoan readyLoan = loanBusinessController.getReadyLoan(authInfo, lmsLoanId, language);
        return ReadyLoanV1Converter.convert(readyLoan);
    }

    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }
}
