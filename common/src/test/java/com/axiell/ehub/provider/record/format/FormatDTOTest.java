package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.provider.dto.FormatDTO;

public class FormatDTOTest extends DTOTestFixture<FormatDTO> {

    @Override
    protected FormatDTO getTestInstance() {
        return FormatBuilder.downloadableFormat().toDTO();
    }

    @Override
    protected Class<FormatDTO> getTestClass() {
        return FormatDTO.class;
    }
}
