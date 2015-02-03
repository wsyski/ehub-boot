package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonAutoDetect
public class CheckoutDTO  {
    private CheckoutMetadataDTO metadata;
    private ContentLinkDTO contentLink;

    public CheckoutMetadataDTO getMetadata() {
        return metadata;
    }

    public CheckoutDTO metadata(CheckoutMetadataDTO metadataDTO) {
        this.metadata = metadataDTO;
        return this;
    }

    public ContentLinkDTO getContentLink() {
        return contentLink;
    }

    public CheckoutDTO contentLink(ContentLinkDTO contentLinkDTO) {
        this.contentLink = contentLinkDTO;
        return this;
    }
}
