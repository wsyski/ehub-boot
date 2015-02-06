package com.axiell.ehub.checkout;

public class ContentLink {
    private final ContentLinkDTO contentLinkDTO;

    public ContentLink(String href) {
        contentLinkDTO = new ContentLinkDTO().href(href);
    }

    public ContentLink(ContentLinkDTO contentLinkDTO) {
        this.contentLinkDTO = contentLinkDTO;
    }

    public String href() {
        return contentLinkDTO.getHref();
    }

    public ContentLinkDTO toDTO() {
        return contentLinkDTO;
    }
}
