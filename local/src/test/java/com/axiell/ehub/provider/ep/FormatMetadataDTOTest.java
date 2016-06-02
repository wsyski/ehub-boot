package com.axiell.ehub.provider.ep;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.ep.FormatDTO;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import com.google.common.collect.Lists;

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
