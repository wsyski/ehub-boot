package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutDTO;

public class CheckoutDTOTest extends DTOTestFixture<CheckoutDTO> {

    @Override
    protected CheckoutDTO getTestInstance() {
        return CheckoutBuilder.checkout().toDTO();
    }

    @Override
    protected Class<CheckoutDTO> getTestClass() {
        return CheckoutDTO.class;
    }
}
