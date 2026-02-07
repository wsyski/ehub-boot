package com.axiell.ehub.lms.arena.controller.patrons.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckoutTestEmediaResponseDTO {
    private String loanId;
    private Fee fee;
    private CheckOutTestSummary.Status status;

    private CheckoutTestEmediaResponseDTO() {
    }

    public CheckoutTestEmediaResponseDTO(final CheckOutTestSummary checkOutTestSummary) {
        this.loanId = checkOutTestSummary.getLoanId();
        this.fee = checkOutTestSummary.getFee();
        this.status = checkOutTestSummary.getStatus();
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(final String loanId) {
        this.loanId = loanId;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(final Fee fee) {
        this.fee = fee;
    }

    public CheckOutTestSummary.Status getStatus() {
        return status;
    }

    public void setStatus(final CheckOutTestSummary.Status status) {
        this.status = status;
    }
}
