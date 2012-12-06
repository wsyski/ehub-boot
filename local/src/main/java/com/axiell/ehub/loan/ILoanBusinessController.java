/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.security.UnauthorizedException;

/**
 * Defines all business methods related to {@link PendingLoan}s and {@link ReadyLoan}s.
 * 
 * <p>This interface should only be used by the resources of the eHUB REST interface or by other business controllers.</p>
 */
public interface ILoanBusinessController {

    /**
     * Creates a loan both in the LMS and at the {@link ContentProvider}.
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param pendingLoan a {@link PendingLoan}
     * @return a {@link ReadyLoan}
     * @throws UnauthorizedException,ForbiddenException
     */
    ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and the globally unique ID of the {@link ReadyLoan}.
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param readyLoanId the globally unique ID of the {@link ReadyLoan}
     * @return a {@link ReadyLoan}
     * @throws UnauthorizedException
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, Long readyLoanId);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and LMS loan ID.
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param lmsLoanId the ID of the loan in the LMS
     * @return a {@link ReadyLoan}
     * @throws UnauthorizedException
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, String lmsLoanId);
}
