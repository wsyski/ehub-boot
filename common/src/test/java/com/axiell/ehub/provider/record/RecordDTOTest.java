package com.axiell.ehub.provider.record;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.controller.external.v5_0.provider.dto.RecordDTO;

public class RecordDTOTest extends DTOTestFixture<RecordDTO> {

    @Override
    protected RecordDTO getTestInstance() {
        return RecordBuilder.record().toDTO();
    }

    @Override
    protected Class<RecordDTO> getTestClass() {
        return RecordDTO.class;
    }
}
