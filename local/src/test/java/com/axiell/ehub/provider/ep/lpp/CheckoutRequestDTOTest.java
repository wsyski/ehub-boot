package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.DTOTestFixture;

public class CheckoutRequestDTOTest extends DTOTestFixture<CheckoutRequestDTO> {

    @Override
    protected CheckoutRequestDTO getTestInstance() {
        return new CheckoutRequestDTO("recordId");
    }

    @Override
    protected Class<CheckoutRequestDTO> getTestClass() {
        return CheckoutRequestDTO.class;
    }
}
