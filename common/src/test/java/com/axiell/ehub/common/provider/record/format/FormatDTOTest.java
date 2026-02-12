package com.axiell.ehub.common.provider.record.format;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.FormatDTO;

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
