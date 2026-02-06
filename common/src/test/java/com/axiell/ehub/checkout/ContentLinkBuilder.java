package com.axiell.ehub.checkout;

import java.util.Collections;

public class ContentLinkBuilder {
    public static final String HREF = "contentLinkHref";

    public static ContentLinks defaultContentLinks() {
        return new ContentLinks(Collections.singletonList(defaultContentLink()));
    }

    public static ContentLink defaultContentLink() {
        return new ContentLink(HREF);
    }
}
