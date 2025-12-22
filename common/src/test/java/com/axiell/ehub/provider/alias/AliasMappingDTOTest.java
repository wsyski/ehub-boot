package com.axiell.ehub.provider.alias;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.provider.dto.AliasMappingDTO;

public class AliasMappingDTOTest extends DTOTestFixture<AliasMappingDTO> {

    @Override
    protected AliasMappingDTO getTestInstance() {
        return AliasMappingDTOBuilder.aliasMappingDTO();
    }

    @Override
    protected Class<AliasMappingDTO> getTestClass() {
        return AliasMappingDTO.class;
    }
}
