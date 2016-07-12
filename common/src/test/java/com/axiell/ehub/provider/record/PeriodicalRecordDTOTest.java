package com.axiell.ehub.provider.record;

import com.axiell.ehub.DTOTestFixture;

public class PeriodicalRecordDTOTest extends DTOTestFixture<RecordDTO> {

    @Override
    protected RecordDTO getTestInstance() {
        return RecordBuilder.periodicalRecord().toDTO();
    }

    @Override
    protected Class<RecordDTO> getTestClass() {
        return RecordDTO.class;
    }
}
