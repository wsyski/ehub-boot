package com.axiell.ehub.core.controller.external.v5_0.checkout;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.Fields;
import com.axiell.ehub.common.FieldsDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.SearchResultDTO;
import com.axiell.ehub.common.checkout.Checkout;
import com.axiell.ehub.common.checkout.CheckoutsSearchResult;
import com.axiell.ehub.core.loan.ILoanBusinessController;

public class CheckoutsResource implements ICheckoutsResource {
    private final ILoanBusinessController loanBusinessController;

    public CheckoutsResource(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        final CheckoutsSearchResult searchResult = loanBusinessController.search(authInfo, lmsLoanId, language);
        return searchResult.toDTO();
    }

    @Override
    public CheckoutDTO checkout(AuthInfo authInfo, FieldsDTO fieldsDTO, String language) {
        Fields fields = new Fields(fieldsDTO);
        Checkout checkout = loanBusinessController.checkout(authInfo, fields, language);
        return checkout.toDTO();
    }

    @Override
    public CheckoutDTO getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        Checkout checkout = loanBusinessController.getCheckout(authInfo, ehubCheckoutId, language);
        return checkout.toDTO();
    }
}
