package com.axiell.ehub.provider.alias;

import com.axiell.ehub.DTOTestFixture;

import java.util.Collections;

public class AliasMappingsDTOTest extends DTOTestFixture<AliasMappingsDTO> {

    @Override
    protected AliasMappingsDTO getTestInstance() {
        return new AliasMappingsDTO(Collections.singleton(AliasMappingDTOBuilder.aliasMappingDTO()));
    }

    @Override
    protected Class<AliasMappingsDTO> getTestClass() {
        return AliasMappingsDTO.class;
    }
}
