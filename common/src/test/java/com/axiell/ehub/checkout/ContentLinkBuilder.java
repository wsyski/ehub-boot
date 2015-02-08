package com.axiell.ehub.checkout;

public class ContentLinkBuilder {
    public static final String HREF = "href";

    public static ContentLink defaultContentLink() {
        return new ContentLink(HREF);
    }
}
