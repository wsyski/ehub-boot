package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.FormatDTO;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;

    private Map<String, FormatMetadataDTO> formats;

    private Date expirationDate;

    public String getId() {
        return id;
    }

    public Map<String, FormatMetadataDTO> getFormats() {
        return formats;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutDTO(final String id, final Map<String, FormatMetadataDTO> formats, final Date expirationDate) {
        this.id = id;
        this.formats = formats;
        this.expirationDate = expirationDate;
    }

    private CheckoutDTO() {
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutDTO)) {
            return false;
        }
        final CheckoutDTO rhs = (CheckoutDTO) obj;
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
