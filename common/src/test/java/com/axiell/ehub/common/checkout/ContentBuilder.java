package com.axiell.ehub.common.checkout;

public class ContentBuilder {
    public static Content contentWithSupplementLinks() {
        return new Content(ContentLinkBuilder.defaultContentLinks()).supplementLinks(SupplementLinkBuilder.defaultSupplementLinks());
    }

    public static Content content() {
        return new Content(ContentLinkBuilder.defaultContentLinks());
    }
}
