/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.security.AuthInfo;

/**
 * Defines all business methods related to PendingLoan}s and ReadyLoans.
 * <p/>
 * <p>This interface should only be used by the resources of the eHUB REST interface or by other business controllers.</p>
 */
public interface ILoanBusinessController {

    /**
     * Creates a loan both in the LMS and at the ContentProvider.
     *
     * @param authInfo    an AuthInfo containing an eHUB consumer ID, library card and pin
     * @param pendingLoan a PendingLoan
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a ReadyLoan
     * @throws UnauthorizedException, ForbiddenException
     */
    ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan, String language);

    /**
     * Gets a ReadyLoan for the provided AuthInfo and the globally unique ID of the ReadyLoan.
     *
     * @param authInfo    an AuthInfo containing an eHUB consumer ID, library card and pin
     * @param readyLoanId the globally unique ID of the ReadyLoan
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a ReadyLoan
     * @throws UnauthorizedException
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, Long readyLoanId, String language);

    /**
     * Gets a ReadyLoan for the provided AuthInfo and LMS loan ID.
     *
     * @param authInfo  an AuthInfo containing an eHUB consumer ID, library card and pin
     * @param lmsLoanId the ID of the loan in the LMS
     * @param language  the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a ReadyLoan
     * @throws UnauthorizedException
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, String lmsLoanId, String language);
}
