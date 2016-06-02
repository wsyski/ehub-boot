package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplementLinkDTO {
    private String name;
    private String href;

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public SupplementLinkDTO name(final String name) {
        this.name = name;
        return this;
    }

    public SupplementLinkDTO href(final String href) {
        this.href = href;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SupplementLinkDTO)) {
            return false;
        }
        final SupplementLinkDTO rhs = (SupplementLinkDTO) obj;
        return new EqualsBuilder().append(getHref(), rhs.getHref()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getHref()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
