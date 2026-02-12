package com.axiell.ehub.local.provider.ep.lpp;

import com.axiell.ehub.common.util.JacksonTimestampInIso8601FormatSerializer;
import com.axiell.ehub.local.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.local.provider.ep.ICheckoutDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LppCheckoutDTO implements ICheckoutDTO {
    private String id;

    @JsonProperty("formatMetadata")
    private Map<String, FormatMetadataDTO> formatMetadatas;
    @JsonSerialize(using = JacksonTimestampInIso8601FormatSerializer.class)
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public Map<String, FormatMetadataDTO> getFormatMetadatas() {
        return formatMetadatas;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public LppCheckoutDTO(final String id, final Map<String, FormatMetadataDTO> formatMetadatas, final Date expirationDate) {
        this.id = id;
        this.formatMetadatas = formatMetadatas;
        this.expirationDate = expirationDate;
    }

    private LppCheckoutDTO() {
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LppCheckoutDTO)) {
            return false;
        }
        final LppCheckoutDTO rhs = (LppCheckoutDTO) obj;
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
