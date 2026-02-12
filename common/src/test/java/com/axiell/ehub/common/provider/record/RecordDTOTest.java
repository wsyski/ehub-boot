package com.axiell.ehub.common.provider.record;

import com.axiell.ehub.common.DTOTestFixture;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.RecordDTO;

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
