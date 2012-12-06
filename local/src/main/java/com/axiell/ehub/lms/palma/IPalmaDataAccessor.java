/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;

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
     * @param libraryCard the library card of the end-user who wants to checkout this media
     * @param pin the pin of the library card
     * @return a {@link PreCheckoutAnalysis}
     * @throws ForbiddenException
     */
    PreCheckoutAnalysis preCheckout(EhubConsumer ehubConsumer, PendingLoan pendingLoan, String libraryCard, String pin);

    /**
     * Does a checkout in the LMS of the media defined in the provided {@link PendingLoan}.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to be used
     * @param pendingLoan the {@link PendingLoan} containing media information
     * @param libraryCard the library card of the end-user who wants to checkout this media
     * @param pin the pin of the library card
     * @return an {@link LmsLoan}
     * @throws ForbiddenException
     */
    LmsLoan checkout(EhubConsumer ehubConsumer, PendingLoan pendingLoan, String libraryCard, String pin);
}
