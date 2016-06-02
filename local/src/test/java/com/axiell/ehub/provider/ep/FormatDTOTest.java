package com.axiell.ehub.provider.ep;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;

public class FormatDTOTest extends DTOTestFixture<FormatDTO> {

    @Override
    protected FormatDTO getTestInstance() {
        return new FormatDTO(FormatBuilder.downloadableFormat().id());
    }

    @Override
    protected Class<FormatDTO> getTestClass() {
        return FormatDTO.class;
    }
}
