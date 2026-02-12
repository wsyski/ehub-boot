package com.axiell.ehub.local.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateLoanResponse {
    @JsonProperty("CreatedLoan")
    private CreatedLoan createdLoan;

    public CreatedLoan getCreatedLoan() {
        return createdLoan;
    }
}
