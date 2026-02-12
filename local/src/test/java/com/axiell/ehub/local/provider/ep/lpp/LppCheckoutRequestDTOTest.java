package com.axiell.ehub.local.provider.ep.lpp;

import com.axiell.ehub.common.DTOTestFixture;

public class LppCheckoutRequestDTOTest extends DTOTestFixture<LppCheckoutRequestDTO> {

    @Override
    protected LppCheckoutRequestDTO getTestInstance() {
        return new LppCheckoutRequestDTO("recordId");
    }

    @Override
    protected Class<LppCheckoutRequestDTO> getTestClass() {
        return LppCheckoutRequestDTO.class;
    }
}
