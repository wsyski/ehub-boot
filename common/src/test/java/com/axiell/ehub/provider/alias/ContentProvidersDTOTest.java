package com.axiell.ehub.provider.alias;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.provider.dto.AliasMappingDTO;
import com.axiell.ehub.controller.external.v5_0.provider.dto.ContentProvidersDTO;

import java.util.Collections;

public class ContentProvidersDTOTest extends DTOTestFixture<ContentProvidersDTO> {

    @Override
    protected ContentProvidersDTO getTestInstance() {
        AliasMappingDTO aliasMapping = AliasMappingDTOBuilder.aliasMappingDTO();
        AliasMappings aliasMappings = AliasMappings.fromAliasMappingDTOCollection(Collections.singleton(aliasMapping));
        return aliasMappings.toContentProvidersDTO();
    }

    @Override
    protected Class<ContentProvidersDTO> getTestClass() {
        return ContentProvidersDTO.class;
    }
}
