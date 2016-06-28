package com.axiell.ehub;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FieldsDTO {
    private final Map<String, String> fields;

    public FieldsDTO() {
        fields = new HashMap<>();
    }

    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldsDTO)) {
            return false;
        }
        final FieldsDTO rhs = (FieldsDTO) obj;
        return new EqualsBuilder().append(getFields(), rhs.getFields()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getFields()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
