package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.checkout.CheckoutDTO;
import com.axiell.ehub.checkout.ContentLinkDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CheckoutDTO_v2 {
    private CheckoutMetadataDTO_v2 metadata;
    private ContentLinkDTO contentLink;

    static CheckoutDTO_v2 fromDTO(CheckoutDTO checkoutDTO) {
        CheckoutDTO_v2 checkoutDTO_v2 = new CheckoutDTO_v2().metadata(CheckoutMetadataDTO_v2.fromDTO(checkoutDTO.getMetadata()));
        List<ContentLinkDTO> contentLinksDTO = checkoutDTO.getContentLinks();
        if (contentLinksDTO.size() > 0) {
            checkoutDTO_v2.contentLink(contentLinksDTO.get(0));
        }
        return checkoutDTO_v2;
    }

    public CheckoutMetadataDTO_v2 getMetadata() {
        return metadata;
    }

    public CheckoutDTO_v2 metadata(CheckoutMetadataDTO_v2 metadataDTO) {
        this.metadata = metadataDTO;
        return this;
    }

    public ContentLinkDTO getContentLink() {
        return contentLink;
    }

    public CheckoutDTO_v2 contentLink(ContentLinkDTO contentLinkDTO) {
        this.contentLink = contentLinkDTO;
        return this;
    }
}
