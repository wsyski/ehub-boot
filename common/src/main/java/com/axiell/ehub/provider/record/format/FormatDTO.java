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
    private Set<String> platforms=new HashSet<>();

    public FormatDTO() {
    }

    public String getId() {
        return id;
    }

    public FormatDTO id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormatDTO name(String name) {
        this.name = name;
        return this;
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

    public FormatDTO contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
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
