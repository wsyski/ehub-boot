package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.checkout.SupplementLink;
import com.axiell.ehub.common.checkout.SupplementLinks;

import java.util.Collections;

public class SupplementLinkBuilder {
    public static final String NAME = "name";
    public static final String HREF = "supplementLinkHref";

    public static SupplementLinks defaultSupplementLinks() {
        return new SupplementLinks(Collections.singletonList(defaultSupplementLink()));
    }

    public static SupplementLink defaultSupplementLink() {
        return new SupplementLink(NAME,HREF);
    }
}
