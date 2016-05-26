package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentLinkDTO implements Serializable {
    private String href;

    public String getHref() {
        return href;
    }

    public ContentLinkDTO href(String href) {
        this.href = href;
        return this;
    }
}
