package com.axiell.ehub.local.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

class SupplementDTO {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Link")
    private String href;

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    SupplementDTO name(final String name) {
        this.name = name;
        return this;
    }

    SupplementDTO href(final String href) {
        this.href = href;
        return this;
    }
}
