package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

import java.util.List;

public class CheckoutsResource implements ICheckoutsResource {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override    
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        return null;
    }

    @Override
    public CheckoutDTO checkout(AuthInfo authInfo, FieldsDTO fields, String language) {
        return null;
    }

    @Override
    public CheckoutDTO getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        return null;
    }
}
