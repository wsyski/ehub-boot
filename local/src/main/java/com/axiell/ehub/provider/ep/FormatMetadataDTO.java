package com.axiell.ehub.provider.ep;

import com.axiell.ehub.controller.external.v5_0.checkout.dto.ContentLinkDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SupplementLinkDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatMetadataDTO {
    private List<ContentLinkDTO> contentLinks;
    private List<SupplementLinkDTO> supplementLinks;

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks;
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return supplementLinks;
    }

    public FormatMetadataDTO contentLinks(final List<ContentLinkDTO> contentLinks) {
        this.contentLinks = contentLinks;
        return this;
    }

    public FormatMetadataDTO supplementLinks(final List<SupplementLinkDTO> supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormatMetadataDTO)) {
            return false;
        }
        final FormatMetadataDTO rhs = (FormatMetadataDTO) obj;
        return new EqualsBuilder().append(getContentLinks(), rhs.getContentLinks()).append(getSupplementLinks(), rhs.getSupplementLinks()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getContentLinks()).append(getSupplementLinks()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
