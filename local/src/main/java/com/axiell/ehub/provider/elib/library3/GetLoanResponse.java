package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class GetLoanResponse {
    @JsonProperty("Loan")
    private LoanDTO loan;

    public LoanDTO getLoan() {
        return loan;
    }
}
