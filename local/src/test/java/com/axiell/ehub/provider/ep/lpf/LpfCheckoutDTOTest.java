package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.DTOTestFixture;

public class LpfCheckoutDTOTest extends DTOTestFixture<LpfCheckoutDTO> {

    @Override
    protected LpfCheckoutDTO getTestInstance() {
        return LppCheckoutDTOBuilder.defaultCheckoutDTO();
    }

    @Override
    protected Class<LpfCheckoutDTO> getTestClass() {
        return LpfCheckoutDTO.class;
    }
}
