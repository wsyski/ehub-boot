package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplementLinkDTO {
    private String name;
    private String href;

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public SupplementLinkDTO name(final String name) {
        this.name = name;
        return this;
    }

    public SupplementLinkDTO href(final String href) {
        this.href = href;
        return this;
    }
}
