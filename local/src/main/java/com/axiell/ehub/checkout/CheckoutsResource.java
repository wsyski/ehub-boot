package com.axiell.ehub.checkout;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

public class CheckoutsResource implements ICheckoutsResource {
    private final ILoanBusinessController loanBusinessController;
    private final AuthInfo authInfo;

    public CheckoutsResource(ILoanBusinessController loanBusinessController, AuthInfo authInfo) {
        this.loanBusinessController = loanBusinessController;
        this.authInfo = authInfo;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(String lmsLoanId, String language) {
        return null;
    }

    @Override
    public CheckoutDTO checkout(FieldsDTO fields, String language) {

        return null;
    }

    @Override
    public CheckoutDTO getCheckout(Long ehubCheckoutId, String language) {
        return null;
    }
}
