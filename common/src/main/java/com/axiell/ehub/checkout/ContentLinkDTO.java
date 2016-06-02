package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentLinkDTO implements Serializable {
    private String href;

    public String getHref() {
        return href;
    }

    public ContentLinkDTO href(String href) {
        this.href = href;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ContentLinkDTO)) {
            return false;
        }
        final ContentLinkDTO rhs = (ContentLinkDTO) obj;
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
