package com.axiell.ehub.checkout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContentLinks {
    private final ContentLinksDTO contentLinksDTO;

    public ContentLinks(final ContentLinksDTO contentLinksDTO) {
        this.contentLinksDTO = contentLinksDTO;
    }

    public ContentLinks(final List<ContentLink> contentLinks) {
        this.contentLinksDTO = new ContentLinksDTO()
                .contentLinks(contentLinks.stream().map(contentLink -> new ContentLinkDTO().href(contentLink.href())).collect(Collectors.toList()));
    }

    public ContentLinks(final ContentLink contentLink) {
        this.contentLinksDTO = new ContentLinksDTO().contentLink(new ContentLinkDTO().href(contentLink.href()));
    }

    public List<ContentLink> getContentLinks() {
        if (contentLinksDTO == null) {
            return new ArrayList<>();
        } else {
            return contentLinksDTO.getContentLinks().stream().map(ContentLink::new).collect(Collectors.toList());
        }
    }

    public ContentLinksDTO toDTO() {
        return contentLinksDTO;
    }
}
