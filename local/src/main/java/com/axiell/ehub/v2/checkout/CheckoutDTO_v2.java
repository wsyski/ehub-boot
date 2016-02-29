package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.checkout.CheckoutDTO;
import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.checkout.ContentLinksDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CheckoutDTO_v2 {
    private CheckoutMetadataDTO metadata;
    private ContentLinkDTO contentLink;

    public static CheckoutDTO_v2 fromDTO(CheckoutDTO checkoutDTO) {
        CheckoutDTO_v2 checkoutDTO_v2 = new CheckoutDTO_v2().metadata(checkoutDTO.getMetadata());
        ContentLinksDTO contentLinksDTO = checkoutDTO.getContentLinks();
        if (contentLinksDTO.size() > 0) {
            checkoutDTO_v2.contentLink(contentLinksDTO.getContentLinks().get(0));
        }
        return checkoutDTO_v2;
    }

    public CheckoutMetadataDTO getMetadata() {
        return metadata;
    }

    public CheckoutDTO_v2 metadata(CheckoutMetadataDTO metadataDTO) {
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
