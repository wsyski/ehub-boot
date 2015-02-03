package com.axiell.ehub.checkout;

public class ContentLink {
    private final ContentLinkDTO contentLinkDTO;

    public ContentLink(ContentLinkDTO contentLinkDTO) {
        this.contentLinkDTO = contentLinkDTO;
    }

    public String getHref() {
        return contentLinkDTO.getHref();
    }
}
