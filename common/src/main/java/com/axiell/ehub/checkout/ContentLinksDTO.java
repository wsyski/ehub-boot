package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentLinksDTO {
    private List<ContentLinkDTO> contentLinks;

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks;
    }

    public ContentLinksDTO contentLinks(List<ContentLinkDTO> contentLinks) {
        this.contentLinks = contentLinks;
        return this;
    }

    public ContentLinksDTO contentLink(ContentLinkDTO contentLink) {
        this.contentLinks = Collections.singletonList(contentLink);
        return this;
    }

    @JsonIgnore
    public int size() {
        return contentLinks == null ? 0 : contentLinks.size();
    }
}
