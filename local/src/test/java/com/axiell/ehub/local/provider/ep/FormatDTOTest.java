package com.axiell.ehub.local.provider.ep;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.provider.record.format.FormatBuilder;

public class FormatDTOTest extends DTOTestFixture<FormatDTO> {

    @Override
    protected FormatDTO getTestInstance() {
        return new FormatDTO(FormatBuilder.downloadableFormat().getId());
    }

    @Override
    protected Class<FormatDTO> getTestClass() {
        return FormatDTO.class;
    }
}
