package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetLoanResponse {
    @JsonProperty("Loan")
    private LoanDTO loan;

    public LoanDTO getLoan() {
        return loan;
    }
}
