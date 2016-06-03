package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import com.axiell.ehub.provider.record.format.FormatDTO;

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
