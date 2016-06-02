package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;

public class LpfLppCheckoutDTOTest extends DTOTestFixture<LpfCheckoutDTO> {

    @Override
    protected LpfCheckoutDTO getTestInstance() {
        return CheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LpfCheckoutDTO> getTestClass() {
        return LpfCheckoutDTO.class;
    }
}
