package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.ehub.local.provider.ep.ICheckoutRequestDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LpfCheckoutRequestDTO implements ICheckoutRequestDTO {
    private String recordId;
    private String formatId;

    public LpfCheckoutRequestDTO(final String recordId, final String formatId) {
        this.recordId = recordId;
        this.formatId = formatId;
    }

    private LpfCheckoutRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public String getFormatId() {
        return formatId;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LpfCheckoutRequestDTO)) {
            return false;
        }
        final LpfCheckoutRequestDTO rhs = (LpfCheckoutRequestDTO) obj;
        return new EqualsBuilder().append(getRecordId(), rhs.getRecordId()).append(getFormatId(), rhs.getFormatId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getRecordId()).append(getFormatId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
