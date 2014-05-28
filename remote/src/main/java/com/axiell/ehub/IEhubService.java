/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.security.AuthInfo;

/**
 * Defines the only public interface of the eHUB.
 */
public interface IEhubService {

    /**
     * Gets the {@link Formats} for a specific record at a certain {@link ContentProvider}.
     *
     * @param authInfo                an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param contentProviderName     the name of the {@link ContentProvider}
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}
     * @param language                the ISO 639 alpha-2 or alpha-3 language code used when getting the translated names and descriptions of the {@link Formats}
     * @return a instance of {@link Formats}
     * @throws EhubException if an exception occurred when trying to get the formats
     */
    Formats getFormats(AuthInfo authInfo, String contentProviderName, String contentProviderRecordId, String language) throws EhubException;

    /**
     * Creates a loan both in the LMS and at the {@link ContentProvider}.
     *
     * @param authInfo    an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param pendingLoan a {@link PendingLoan}
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ReadyLoan}
     * @throws EhubException if an exception occurred when trying to create a loan
     */
    ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan, String language) throws EhubException;

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and the globally unique ID of the {@link ReadyLoan}.
     *
     * @param authInfo    an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param readyLoanId the globally unique ID of the {@link ReadyLoan}
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ReadyLoan}
     * @throws EhubException if an exception occurred when trying to get a loan
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, Long readyLoanId, String language) throws EhubException;

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and LMS loan ID.
     *
     * @param authInfo  an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param lmsLoanId the ID of the loan in the LMS
     * @param language  the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ReadyLoan}
     * @throws EhubException if an exception occurred when trying to get a loan
     */
    ReadyLoan getReadyLoan(AuthInfo authInfo, String lmsLoanId, String language) throws EhubException;
}
