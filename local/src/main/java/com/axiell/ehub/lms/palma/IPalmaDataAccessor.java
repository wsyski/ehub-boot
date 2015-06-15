/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.security.AuthInfo;

import java.util.Date;

/**
 * Defines the PALMA methods used by the eHUB.
 */
public interface IPalmaDataAccessor {

    /**
     * Performs a pre-checkout analysis. It means that a check is made in the LMS to see if the end-user is allowed to
     * checkout the media at all, if the end-user already has borrowed the media or if it would be a new loan in the
     * LMS.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to be used
     * @param pendingLoan the {@link PendingLoan} containing media information
     * @param patron
     * @return a {@link CheckoutTestAnalysis}
     * @throws ForbiddenException
     */
    CheckoutTestAnalysis checkoutTest(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Patron patron);

    /**
     * Does a checkout in the LMS of the media defined in the provided {@link PendingLoan}.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to be used
     * @param pendingLoan the {@link PendingLoan} containing media information
     * @param expirationDate the loan expiration date
     * @param patron
     * @return an {@link LmsLoan}
     * @throws ForbiddenException
     */
    LmsLoan checkout(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Date expirationDate, Patron patron);

    String getMediaClass(EhubConsumer ehubConsumer, String contentProviderAlias, String contentProviderRecordId);
}
