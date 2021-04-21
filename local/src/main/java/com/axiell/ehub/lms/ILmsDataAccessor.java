/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms;

import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.authinfo.Patron;

import java.util.Date;
import java.util.Locale;

/**
 * Defines the PALMA methods used by the eHUB.
 */
public interface ILmsDataAccessor {

    /**
     * Performs a pre-checkout analysis. It means that a check is made in the LMS to see if the end-user is allowed to
     * checkout the media at all, if the end-user already has borrowed the media or if it would be a new loan in the
     * LMS.
     *
     * @param ehubConsumer the {@link EhubConsumer} to be used
     * @param pendingLoan the {@link PendingLoan} containing media information
     * @param patron
     * @param isLoanPerProduct
     * @return a {@link CheckoutTestAnalysis}
     * @throws ForbiddenException
     */
    CheckoutTestAnalysis checkoutTest(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Patron patron, boolean isLoanPerProduct, Locale locale);

    /**
     * Does a checkout in the LMS of the media defined in the provided {@link PendingLoan}.
     *
     * @param ehubConsumer the {@link EhubConsumer} to be used
     * @param pendingLoan the {@link PendingLoan} containing media information
     * @param expirationDate the loan expiration date
     * @param patron
     * @param isLoanPerProduct
     * @return an {@link LmsLoan}
     * @throws ForbiddenException
     */
    LmsLoan checkout(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Date expirationDate, Patron patron, boolean isLoanPerProduct, Locale locale);

    String getMediaClass(EhubConsumer ehubConsumer, String contentProviderAlias, String contentProviderRecordId, Locale locale);
}
