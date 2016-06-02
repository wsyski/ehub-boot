package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.DTOTestFixture;

public class LpfLppCheckoutDTOTest extends DTOTestFixture<LppCheckoutDTO> {

    @Override
    protected LppCheckoutDTO getTestInstance() {
        return CheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LppCheckoutDTO> getTestClass() {
        return LppCheckoutDTO.class;
    }
}
