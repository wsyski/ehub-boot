package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class ContentLinkDTO {
    private String href;

    public String getHref() {
        return href;
    }

    public ContentLinkDTO href(String href) {
        this.href = href;
        return this;
    }
}
