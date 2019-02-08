package com.axiell.ehub.provider.alias;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.ContentProvidersDTO;

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
