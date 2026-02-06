package com.axiell.ehub.controller.external.v5_0.checkout;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SearchResultDTO;

public class CheckoutsResource implements ICheckoutsResource {

    @Override
    public SearchResultDTO<CheckoutMetadataDTO> search(AuthInfo authInfo, String lmsLoanId, String language) {
        return new SearchResultDTO<>();
    }

    @Override
    public CheckoutDTO checkout(AuthInfo authInfo, FieldsDTO fieldsDTO, String language) {
        return null;
    }

    @Override
    public CheckoutDTO getCheckout(AuthInfo authInfo, Long ehubCheckoutId, String language) {
        return null;
    }
}
