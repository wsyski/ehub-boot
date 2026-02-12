package com.axiell.ehub.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import static com.axiell.ehub.common.ErrorCause.MISSING_FIELD;
import static com.axiell.ehub.common.ErrorCauseArgument.Type.FIELD;

public class Fields {
    private final FieldsDTO dto;

    public Fields() {
        dto = new FieldsDTO();
    }

    public Fields(FieldsDTO dto) {
        this.dto = dto;
    }

    public Fields addValue(final String key, final String value) {
        if (value!=null) {
            dto.getFields().put(key, value);
        }
        return this;
    }

    public String getValue(String key) {
        return dto.getFields().get(key);
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

    public String getOptionalValue(String key) {
        return getValue(key);
    }

    public FieldsDTO toDTO() {
        return dto;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fields)) {
            return false;
        }
        final Fields rhs = (Fields) obj;
        return new EqualsBuilder().append(toDTO(), rhs.toDTO()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(toDTO()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
