package com.axiell.ehub;

import static com.axiell.ehub.ErrorCause.MISSING_FIELD;
import static com.axiell.ehub.ErrorCauseArgument.Type.FIELD;

public class Fields {
    private final FieldsDTO dto;

    public Fields() {
        dto = new FieldsDTO();
    }

    public Fields(FieldsDTO dto) {
        this.dto = dto;
    }

    public void addValue(String key, String value) {
        dto.getFields().put(key, value);
    }

    public String getRequiredValue(String key) {
        String value = getValue(key);
        if (value == null)
            throw makeMissingFieldException(key);
        return value;
    }

    private BadRequestException makeMissingFieldException(String key) {
        final ErrorCauseArgument argument = new ErrorCauseArgument(FIELD, key);
        return new BadRequestException(MISSING_FIELD, argument);
    }

    private String getValue(String key) {
        return dto.getFields().get(key);
    }

    public String getOptionalValue(String key) {
        return getValue(key);
    }

    public FieldsDTO toDTO() {
        return dto;
    }
}
