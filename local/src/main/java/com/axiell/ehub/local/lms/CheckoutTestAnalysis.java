/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.local.lms;

import com.axiell.ehub.common.InternalServerErrorException;
import org.apache.commons.lang3.Validate;

/**
 * Represents an analysis of a checkout before it is made in the LMS.
 */
public final class CheckoutTestAnalysis {
    private final Result result;
    private final String lmsLoanId;

    /**
     * Constructs a new {@link CheckoutTestAnalysis}.
     *
     * @param result    the result of the analysis
     * @param lmsLoanId the ID of the LMS loan if the result is {@link Result#ACTIVE_LOAN}, <code>null</code> otherwise
     */
    public CheckoutTestAnalysis(final Result result, final String lmsLoanId) {
        Validate.notNull(result, "Pre-checkout result can't be null");
        this.result = result;
        if (Result.ACTIVE_LOAN.equals(result) && lmsLoanId == null) {
            throw new InternalServerErrorException("Invalid pre-checkout analysis - active loan but no LMS loan ID");
        }
        this.lmsLoanId = lmsLoanId;
    }

    /**
     * Returns the {@link Result}.
     *
     * @return the {@link Result}
     */
    public Result getResult() {
        return result;
    }

    /**
     * Returns the ID of the LMS loan if the result of the analysis is {@link Result#ACTIVE_LOAN}, <code>null</code>
     * otherwise.
     *
     * @return the ID of an LMS loan
     */
    public String getLmsLoanId() {
        return lmsLoanId;
    }

    /**
     * Represents a result of a pre-checkout analysis.
     */
    public static enum Result {
        NEW_LOAN, ACTIVE_LOAN;
    }
}
