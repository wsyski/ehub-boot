package com.axiell.ehub.controller.external.v5_0.provider.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Set;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentProviderDTO {
    private String name;
    private Set<String> aliases;

    private ContentProviderDTO() {
    }

    public ContentProviderDTO(String name, Set<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ContentProviderDTO)) {
            return false;
        }
        final ContentProviderDTO rhs = (ContentProviderDTO) obj;
        return new EqualsBuilder().append(getName(), rhs.getName()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getName()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
