package com.axiell.ehub.lms.arena.controller.patrons.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CheckoutEmediaResponseDTO {
    private String loanId;
    private String recordId;
    private LocalDate dueDate;
    private Fee fee;

    private CheckoutEmediaResponseDTO() {
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(final String loanId) {
        this.loanId = loanId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(final Fee fee) {
        this.fee = fee;
    }
}
