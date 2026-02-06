package com.axiell.ehub.checkout;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;

public class CheckoutMetadataDTOTest extends DTOTestFixture<CheckoutMetadataDTO> {

    @Override
    protected CheckoutMetadataDTO getTestInstance() {
        return CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat().toDTO();
    }

    @Override
    protected Class<CheckoutMetadataDTO> getTestClass() {
        return CheckoutMetadataDTO.class;
    }
}
