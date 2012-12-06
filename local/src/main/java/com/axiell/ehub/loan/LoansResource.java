/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.security.AuthInfo;

/**
 * Default implementation of the {@link ILoansResource}.
 */
public final class LoansResource implements ILoansResource {
    private ILoanBusinessController loanBusinessController;

    /**
     * @see com.axiell.ehub.loan.ILoansResource#createLoan(com.axiell.ehub.security.AuthInfo, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    public ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan) {
        return loanBusinessController.createLoan(authInfo, pendingLoan);
    }

    /**
     * @see com.axiell.ehub.loan.ILoansResource#getLoan(com.axiell.ehub.security.AuthInfo, java.lang.Long)
     */
    @Override
    public ReadyLoan getLoan(AuthInfo authInfo, Long readyLoanId) {
        return loanBusinessController.getReadyLoan(authInfo, readyLoanId);
    }
    
    /**
     * @see com.axiell.ehub.loan.ILoansResource#getLoan(com.axiell.ehub.security.AuthInfo, java.lang.String)
     */
    @Override
    public ReadyLoan getLoan(AuthInfo authInfo, String lmsLoanId) {
        return loanBusinessController.getReadyLoan(authInfo, lmsLoanId);
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
