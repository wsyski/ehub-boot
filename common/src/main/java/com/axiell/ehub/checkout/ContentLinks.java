package com.axiell.ehub.checkout;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContentLinks implements Serializable {
    private List<ContentLinkDTO> contentLinksDTO = new ArrayList<>();

    public ContentLinks(final List<ContentLink> contentLinks) {
        if (contentLinks != null) {
            this.contentLinksDTO = contentLinks.stream()
                    .map(contentLink -> new ContentLinkDTO().href(contentLink.href())).collect(Collectors.toList());
        }
    }

    public ContentLinks(final ContentLink contentLink) {
        this.contentLinksDTO = Collections.singletonList(new ContentLinkDTO().href(contentLink.href()));
    }

    public List<ContentLink> getContentLinks() {
        return contentLinksDTO.stream().map(ContentLink::new).collect(Collectors.toList());
    }

    public List<String> hrefs() {
        return contentLinksDTO.stream().map(ContentLinkDTO::getHref).collect(Collectors.toList());
    }

    private ContentLinks() {
    }

    private ContentLinks contentLinksDTO(final List<ContentLinkDTO> contentLinksDTO) {
        this.contentLinksDTO = contentLinksDTO;
        return this;
    }

    public List<ContentLinkDTO> toDTO() {
        return contentLinksDTO;
    }

    public static ContentLinks fromDTO(final List<ContentLinkDTO> contentLinksDTO) {
        return new ContentLinks().contentLinksDTO(contentLinksDTO);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
