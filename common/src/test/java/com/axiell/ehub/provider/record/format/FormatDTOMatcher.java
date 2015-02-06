package com.axiell.ehub.provider.record.format;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class FormatDTOMatcher extends BaseMatcher<FormatDTO> {
    private final FormatDTO expFormatDTO;

    private FormatDTOMatcher(FormatDTO expFormatDTO) {
        this.expFormatDTO = expFormatDTO;
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof FormatDTO) {
            FormatDTO actFormatDTO = (FormatDTO) item;
            return expFormatDTO.getId().equals(actFormatDTO.getId()) &&
                    expFormatDTO.getDescription().equals(actFormatDTO.getDescription()) &&
                    expFormatDTO.getName().equals(actFormatDTO.getName()) &&
                    expFormatDTO.getContentDisposition().equals(actFormatDTO.getContentDisposition()) &&
                    expFormatDTO.getPlayerWidth() == actFormatDTO.getPlayerWidth() &&
                    expFormatDTO.getPlayerHeight() == actFormatDTO.getPlayerHeight();
        } else
            return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a FormatDTO");
    }

    @Factory
    public static FormatDTOMatcher matchesExpectedFormatDTO(FormatDTO expFormatDTO) {
        return new FormatDTOMatcher(expFormatDTO);
    }
}
