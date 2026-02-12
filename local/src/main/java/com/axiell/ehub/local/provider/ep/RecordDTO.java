package com.axiell.ehub.local.provider.ep;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO {
    private String id;
    private List<FormatDTO> formats;


    public RecordDTO(final String id, final List<FormatDTO> formats) {
        this.id = id;
        this.formats = formats;
    }

    private RecordDTO() {
    }

    public String getId() {
        return id;
    }

    public List<FormatDTO> getFormats() {
        return formats;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecordDTO)) {
            return false;
        }
        final RecordDTO rhs = (RecordDTO) obj;
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
