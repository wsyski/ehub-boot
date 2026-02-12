package com.axiell.ehub.local.provider.ep;

import com.axiell.ehub.common.DTOTestFixture;

public class FormatMetadataDTOTest extends DTOTestFixture<FormatMetadataDTO> {

    @Override
    protected FormatMetadataDTO getTestInstance() {
        return FormatMetadataDTOBuilder.defaultFormatMetadataDTO();
    }

    @Override
    protected Class<FormatMetadataDTO> getTestClass() {
        return FormatMetadataDTO.class;
    }
}
