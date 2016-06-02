package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;

public class CheckoutDTOTest extends DTOTestFixture<CheckoutDTO> {

    @Override
    protected CheckoutDTO getTestInstance() {
        return CheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<CheckoutDTO> getTestClass() {
        return CheckoutDTO.class;
    }
}
