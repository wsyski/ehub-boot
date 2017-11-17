package com.axiell.ehub.provider.alias;

import com.axiell.ehub.DTOTestFixture;

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
