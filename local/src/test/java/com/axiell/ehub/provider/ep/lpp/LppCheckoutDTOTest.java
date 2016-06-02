package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.DTOTestFixture;

public class LppCheckoutDTOTest extends DTOTestFixture<LppCheckoutDTO> {

    @Override
    protected LppCheckoutDTO getTestInstance() {
        return LppCheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LppCheckoutDTO> getTestClass() {
        return LppCheckoutDTO.class;
    }
}
