package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateLoanResponse {
    @JsonProperty("CreatedLoan")
    private CreatedLoan createdLoan;

    public CreatedLoan getCreatedLoan() {
        return createdLoan;
    }
}
