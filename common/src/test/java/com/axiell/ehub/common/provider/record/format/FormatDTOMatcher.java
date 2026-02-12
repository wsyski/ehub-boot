package com.axiell.ehub.common.provider.record.format;

import com.axiell.ehub.common.controller.external.v5_0.provider.dto.FormatDTO;
import com.google.common.collect.Sets;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class FormatDTOMatcher extends BaseMatcher<FormatDTO> {
    private final FormatDTO expFormatDTO;

    private FormatDTOMatcher(FormatDTO expFormatDTO) {
        this.expFormatDTO = expFormatDTO;
    }

    public static FormatDTOMatcher matchesExpectedFormatDTO(FormatDTO expFormatDTO) {
        return new FormatDTOMatcher(expFormatDTO);
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof FormatDTO) {
            FormatDTO actFormatDTO = (FormatDTO) item;
            return expFormatDTO.getId().equals(actFormatDTO.getId()) &&
                    expFormatDTO.getDescription().equals(actFormatDTO.getDescription()) &&
                    expFormatDTO.getName().equals(actFormatDTO.getName()) &&
                    expFormatDTO.getContentDisposition().equals(actFormatDTO.getContentDisposition()) &&
                    Sets.symmetricDifference(expFormatDTO.getPlatforms(), actFormatDTO.getPlatforms()).isEmpty();
        } else
            return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a FormatDTO");
    }
}
