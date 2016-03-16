package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class GetLoansResponse {
    @JsonProperty("Loans")
    private List<LoanDTO> loans;

    LoanDTO getLoanWithProductId(String productId) {
        LoanDTO loanWithRequestedProductId = null;
        for (LoanDTO loan : loans) {
            if (productId.equals(loan.getProductId()))
                loanWithRequestedProductId = loan;
        }
        return loanWithRequestedProductId;
    }
}
