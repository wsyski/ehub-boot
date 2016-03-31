package com.axiell.ehub.v2.provider.record.format;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class FormatDTOMatcher_v2 extends BaseMatcher<FormatDTO_v2> {
    private final FormatDTO_v2 expFormatDTO;

    private FormatDTOMatcher_v2(FormatDTO_v2 expFormatDTO) {
        this.expFormatDTO = expFormatDTO;
    }

    @Factory
    public static FormatDTOMatcher_v2 matchesExpectedFormatDTO(FormatDTO_v2 expFormatDTO) {
        return new FormatDTOMatcher_v2(expFormatDTO);
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof FormatDTO_v2) {
            FormatDTO_v2 actFormatDTO = (FormatDTO_v2) item;
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
        description.appendText("matching a FormatDTO_v2");
    }
}