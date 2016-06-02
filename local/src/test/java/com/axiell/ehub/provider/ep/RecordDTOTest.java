package com.axiell.ehub.provider.ep;

import com.axiell.ehub.DTOTestFixture;
import com.axiell.ehub.provider.record.format.FormatBuilder;
import com.google.common.collect.Lists;

public class RecordDTOTest extends DTOTestFixture<RecordDTO> {

    @Override
    protected RecordDTO getTestInstance() {
        return new RecordDTO("recordId", Lists.newArrayList(new FormatDTO(FormatBuilder.FORMAT_ID)));
    }

    @Override
    protected Class<RecordDTO> getTestClass() {
        return RecordDTO.class;
    }
}
