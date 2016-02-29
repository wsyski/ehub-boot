package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.Fields;
import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;

public class CheckoutsResource_v2 implements ICheckoutsResource_v2 {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource_v2(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        final CheckoutsSearchResult searchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        return searchResult.toDTO();
    }

    @Override
    public CheckoutDTO_v2 checkout(AuthInfo authInfo, FieldsDTO fieldsDTO, String language) {
        Fields fields = new Fields(fieldsDTO);
        Checkout checkout = loanBusinessController.checkout(authInfo, fields, language);
        return CheckoutDTO_v2.fromDTO(checkout.toDTO());
    }

    @Override
    public CheckoutDTO_v2 getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        Checkout checkout = loanBusinessController.getCheckout(authInfo, ehubCheckoutId, language);
        return CheckoutDTO_v2.fromDTO(checkout.toDTO());
    }


}
