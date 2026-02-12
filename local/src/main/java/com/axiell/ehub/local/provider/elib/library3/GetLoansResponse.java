package com.axiell.ehub.local.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetLoansResponse {
    @JsonProperty("Loans")
    private List<LoanDTO> loans;

    public LoanDTO getLoanWithProductId(String productId) {
        LoanDTO loanWithRequestedProductId = null;
        for (LoanDTO loan : loans) {
            if (productId.equals(loan.getProductId()))
                loanWithRequestedProductId = loan;
        }
        return loanWithRequestedProductId;
    }
}
