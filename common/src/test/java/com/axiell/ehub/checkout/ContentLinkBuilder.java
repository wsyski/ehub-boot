package com.axiell.ehub.checkout;

public class ContentLinkBuilder {
    public static final String DEFAULT_HREF = "defaultHref";

    private String href = DEFAULT_HREF;

    public static ContentLinkBuilder newContentLinkBuilder() {
        return new ContentLinkBuilder();
    }

    public ContentLinkBuilder withHref(final String href) {
        this.href = href;
        return this;
    }

    public ContentLink build() {
        return new ContentLink(href);
    }

    public static ContentLink defaultContentLink() {
        return newContentLinkBuilder().build();
    }
}
