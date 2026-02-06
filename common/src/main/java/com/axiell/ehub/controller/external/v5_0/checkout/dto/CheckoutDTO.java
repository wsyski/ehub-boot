package com.axiell.ehub.controller.external.v5_0.checkout.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO implements Serializable {
    private CheckoutMetadataDTO metadata;
    private List<ContentLinkDTO> contentLinks;
    private List<SupplementLinkDTO> supplementLinks;

    private CheckoutDTO() {
    }

    public CheckoutDTO(final CheckoutMetadataDTO metadata, final List<ContentLinkDTO> contentLinks) {
        this.metadata = metadata;
        this.contentLinks = contentLinks;
    }

    public CheckoutMetadataDTO getMetadata() {
        return metadata;
    }

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks == null ? new ArrayList<>() : contentLinks;
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return supplementLinks == null ? new ArrayList<>() : supplementLinks;
    }

    public CheckoutDTO supplementLinks(final List<SupplementLinkDTO> supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutDTO)) {
            return false;
        }
        final CheckoutDTO rhs = (CheckoutDTO) obj;
        return new EqualsBuilder().append(getMetadata(), rhs.getMetadata()).append(getContentLinks(),rhs.getContentLinks()).append(getSupplementLinks(),rhs.getSupplementLinks()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getMetadata()).append(getContentLinks()).append(getSupplementLinks()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
