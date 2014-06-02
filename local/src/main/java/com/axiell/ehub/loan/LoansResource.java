/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.HeaderParam;

/**
 * Default implementation of the {@link ILoansResource}.
 */
public final class LoansResource implements ILoansResource {
    private ILoanBusinessController loanBusinessController;

//    @Override
//    public ReadyLoan createLoan(@HeaderParam("Authorization") AuthInfo authInfo, PendingLoan pendingLoan) {
//        return createLoan(authInfo, null, pendingLoan);
//    }

    /**
     * @see ILoansResource#createLoan(com.axiell.ehub.security.AuthInfo, String, PendingLoan)
     */
    @Override
    public ReadyLoan createLoan(AuthInfo authInfo, String language, PendingLoan pendingLoan) {
        return loanBusinessController.createLoan(authInfo, pendingLoan, language);
    }

//    @Override
//    public ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, Long readyLoanId) {
//        return getLoan(authInfo, readyLoanId, null);
//    }

    /**
     * @see ILoansResource#getLoan(com.axiell.ehub.security.AuthInfo, Long, String)
     */
    @Override
    public ReadyLoan getLoan(AuthInfo authInfo, Long readyLoanId, String language) {
        return loanBusinessController.getReadyLoan(authInfo, readyLoanId, language);
    }

//    @Override
//    public ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, String lmsLoanId) {
//        return getLoan(authInfo, lmsLoanId, null);
//    }

    /**
     * @see ILoansResource#getLoan(com.axiell.ehub.security.AuthInfo, String, String)
     */
    @Override
    public ReadyLoan getLoan(AuthInfo authInfo, String lmsLoanId, String language) {
        return loanBusinessController.getReadyLoan(authInfo, lmsLoanId, language);
    }

    /**
     * Sets the {@link ILoanBusinessController}.
     *
     * @param loanBusinessController the {@link ILoanBusinessController} to set
     */
    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }
}
