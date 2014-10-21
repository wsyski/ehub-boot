package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class GetLoansResponse {
    @JsonProperty("Loans")
    private List<Loan> loans;

    Loan getLoanWithProductId(String productId) {
        Loan loanWithRequestedProductId = null;
        for (Loan loan : loans) {
            if (productId.equals(loan.getProductId()))
                loanWithRequestedProductId = loan;
        }
        return loanWithRequestedProductId;
    }
}
