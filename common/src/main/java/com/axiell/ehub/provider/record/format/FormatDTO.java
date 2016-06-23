package com.axiell.ehub.provider.record.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatDTO implements Serializable {
    private String id;
    private String name;
    private boolean locked;
    private String description;
    private ContentDisposition contentDisposition;
    private Set<String> platforms = new HashSet<>();

    private FormatDTO() {
    }

    public FormatDTO(final String id, final String name, final ContentDisposition contentDisposition) {
        this.id = id;
        this.name = name;
        this.contentDisposition = contentDisposition;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isLocked() {
        return locked;
    }

    public FormatDTO locked(final boolean locked) {
        this.locked = locked;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FormatDTO description(String description) {
        this.description = description;
        return this;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }


    public Set<String> getPlatforms() {
        return platforms;
    }

    public FormatDTO platforms(final Set<String> platforms) {
        this.platforms = platforms;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormatDTO)) {
            return false;
        }
        final FormatDTO rhs = (FormatDTO) obj;
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
