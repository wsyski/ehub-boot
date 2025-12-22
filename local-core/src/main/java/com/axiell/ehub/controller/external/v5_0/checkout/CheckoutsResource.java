package com.axiell.ehub.controller.external.v5_0.checkout;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.Fields;
import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SearchResultDTO;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.loan.ILoanBusinessController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
