package com.axiell.ehub.checkout;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;

public class Content {
    private ContentLinks contentLinks;
    private SupplementLinks supplementLinks;

    public Content(final ContentLinks contentLinks) {
        this.contentLinks = contentLinks;
        this.supplementLinks = new SupplementLinks(new ArrayList<>());
    }

    public Content supplementLinks(final SupplementLinks supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    public ContentLinks getContentLinks() {
        return contentLinks;
    }

    public SupplementLinks getSupplementLinks() {
        return supplementLinks;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
