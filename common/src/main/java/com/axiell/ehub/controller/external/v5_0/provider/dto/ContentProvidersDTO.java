package com.axiell.ehub.controller.external.v5_0.provider.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentProvidersDTO {
    private List<ContentProviderDTO> contentProviders;

    private ContentProvidersDTO() {
    }

    public ContentProvidersDTO(List<ContentProviderDTO> contentProviders) {
        this.contentProviders = contentProviders;
    }

    public List<ContentProviderDTO> getContentProviders() {
        return contentProviders;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ContentProvidersDTO)) {
            return false;
        }
        final ContentProvidersDTO rhs = (ContentProvidersDTO) obj;
        return new EqualsBuilder().append(getContentProviders(), rhs.getContentProviders()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getContentProviders()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
