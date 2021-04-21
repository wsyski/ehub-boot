package com.axiell.ehub.lms.arena.resources.portalsites.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordDTO {
    private String id;
    private Long agencyId;
    private String uberkey;
    private Map<String, Object> fields;

    private RecordDTO() {
    }

    private RecordDTO(final Builder builder) {
        this.id = builder.id;
        this.agencyId = builder.agencyId;
        this.uberkey = builder.uberkey;
        this.fields = builder.fields;
    }

    public String getId() {
        return this.id;
    }

    public Long getAgencyId() {
        return this.agencyId;
    }

    public String getUberkey() {
        return this.uberkey;
    }

    public Map<String, Object> getFields() {
        return this.fields;
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

    public static final class Builder implements Serializable {
        String id;
        Long agencyId;
        String uberkey;
        Map<String, Object> fields = new HashMap<>();

        public Builder(final String id, final Long agencyId, final String uberkey) {
            this.id = id;
            this.agencyId = agencyId;
            this.uberkey = uberkey;
        }

        public Builder fields(final Map<String, Object> fields) {
            this.fields = fields;
            return this;
        }

        public RecordDTO build() {
            return new RecordDTO(this);
        }

        @Override
        public final boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Builder)) {
                return false;
            }
            final Builder rhs = (Builder) obj;
            return new EqualsBuilder().append(id, rhs.id).isEquals();
        }

        @Override
        public final int hashCode() {
            return new HashCodeBuilder(17, 31).append(id).toHashCode();
        }
    }
}
