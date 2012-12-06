/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import org.apache.commons.lang3.Validate;

/**
 * Represents an analysis of a checkout before it is made in the LMS.
 */
public final class PreCheckoutAnalysis {
    private final Result status;
    private final String lmsLoanId;

    /**
     * Constructs a new {@link PreCheckoutAnalysis}.
     * 
     * @param result the result of the analysis
     * @param lmsLoanId the ID of the LMS loan if the result is {@link Result#ACTIVE_LOAN}, <code>null</code> otherwise
     */
    PreCheckoutAnalysis(final Result result, final String lmsLoanId) {
        Validate.notNull(result, "Status can't be null");
        this.status = result;
        if (Result.ACTIVE_LOAN.equals(result) && lmsLoanId == null) {
            // LMS Loan ID can't be null when result indicates that it is an active loan
            // TODO: throw appropriate
        }
        this.lmsLoanId = lmsLoanId;
    }

    /**
     * Returns the {@link Result}.
     * 
     * @return the {@link Result}
     */
    public Result getResult() {
        return status;
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
