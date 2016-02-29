package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private CheckoutMetadataDTO metadata;
    private ContentLinksDTO contentLinks;

    public CheckoutMetadataDTO getMetadata() {
        return metadata;
    }

    public CheckoutDTO metadata(CheckoutMetadataDTO metadataDTO) {
        this.metadata = metadataDTO;
        return this;
    }

    public ContentLinksDTO getContentLinks() {
        return contentLinks;
    }

    public CheckoutDTO contentLinks(final ContentLinksDTO contentLinksDTO) {
        this.contentLinks = contentLinksDTO;
        return this;
    }

    public CheckoutDTO contentLink(final ContentLinkDTO contentLinkDTO) {
        this.contentLinks = new ContentLinksDTO().contentLink(contentLinkDTO);
        return this;
    }
}
