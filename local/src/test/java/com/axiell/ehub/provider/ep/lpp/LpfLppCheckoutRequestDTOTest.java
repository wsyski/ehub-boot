package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.DTOTestFixture;

public class LpfLppCheckoutRequestDTOTest extends DTOTestFixture<LppCheckoutRequestDTO> {

    @Override
    protected LppCheckoutRequestDTO getTestInstance() {
        return new LppCheckoutRequestDTO("recordId");
    }

    @Override
    protected Class<LppCheckoutRequestDTO> getTestClass() {
        return LppCheckoutRequestDTO.class;
    }
}
