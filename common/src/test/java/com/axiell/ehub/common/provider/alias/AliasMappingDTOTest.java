package com.axiell.ehub.common.provider.alias;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.AliasMappingDTO;

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
