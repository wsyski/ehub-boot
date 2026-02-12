package com.axiell.ehub.local.lms.arena.controller.patrons.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CheckOutTestSummary {
    private String loanId;
    private Fee fee;
    private Status status;

    public enum Status {
        ACTIVE_LOAN("activeLoan"), BLOCKED_BORR_CARD("blockedBorrCard"), BORR_CARD_NOT_FOUND("borrCardNotFound"), CATALOGUE_RECORD_NOT_FOUND(
                "catalogueRecordNotFound"), CHECK_OUT_DENIED("checkOutDenied"), INVALID_BORR_CARD("invalidBorrCard"), INVALID_ITEM_ID(
                "invalidItemId"), INVALID_PIN_CODE("invalidPinCode"), INVALID_RECORD_ID("invalidRecordId"), NEW_LOAN(
                "newLoan");
        private final String value;

        Status(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static Status fromValue(String v) {
            for (Status c : Status.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
    }

    public CheckOutTestSummary(final String loanId, final Fee fee, final Status status) {
        this.loanId = loanId;
        this.fee = fee;
        this.status = status;
    }

    public final String getLoanId() {
        return loanId;
    }

    public final Fee getFee() {
        return fee;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 37).append(fee).append(loanId).append(status).toHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckOutTestSummary)) {
            return false;
        }
        CheckOutTestSummary rhs = (CheckOutTestSummary) obj;
        return new EqualsBuilder().append(fee, rhs.getFee()).append(loanId, rhs.getLoanId())
                .append(status, rhs.getStatus()).isEquals();
    }
}
