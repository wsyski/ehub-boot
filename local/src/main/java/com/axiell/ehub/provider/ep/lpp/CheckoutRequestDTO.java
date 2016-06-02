package com.axiell.ehub.provider.ep.lpp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequestDTO {
    private String recordId;

    public CheckoutRequestDTO(final String recordId) {
        this.recordId = recordId;
    }

    private CheckoutRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutRequestDTO)) {
            return false;
        }
        final CheckoutRequestDTO rhs = (CheckoutRequestDTO) obj;
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

