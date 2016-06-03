package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.provider.ep.ICheckoutDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LpfCheckoutDTO implements ICheckoutDTO {
    private String id;
    @JsonUnwrapped
    private FormatMetadataDTO formatMetadata = new FormatMetadataDTO();
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public FormatMetadataDTO getFormatMetadata() {
        return formatMetadata;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public LpfCheckoutDTO(final String id, final FormatMetadataDTO formatMetadata, final Date expirationDate) {
        this.id = id;
        this.formatMetadata = formatMetadata;
        this.expirationDate = expirationDate;
    }

    private LpfCheckoutDTO() {
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LpfCheckoutDTO)) {
            return false;
        }
        final LpfCheckoutDTO rhs = (LpfCheckoutDTO) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
