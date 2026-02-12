package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.checkout.ContentLink;
import com.axiell.ehub.common.checkout.ContentLinks;

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
