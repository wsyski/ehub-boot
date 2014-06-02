package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class GetLoanResponse {
    @JsonProperty("Loan")
    private Loan loan;

    public Loan getLoan() {
        return loan;
    }
}
