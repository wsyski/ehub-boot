package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.ICheckoutRequestDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LppCheckoutRequestDTO implements ICheckoutRequestDTO {
    private String recordId;

    public LppCheckoutRequestDTO(final String recordId) {
        this.recordId = recordId;
    }

    private LppCheckoutRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LppCheckoutRequestDTO)) {
            return false;
        }
        final LppCheckoutRequestDTO rhs = (LppCheckoutRequestDTO) obj;
        return new EqualsBuilder().append(getRecordId(), rhs.getRecordId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getRecordId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

