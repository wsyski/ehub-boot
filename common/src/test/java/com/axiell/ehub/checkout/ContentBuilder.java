package com.axiell.ehub.checkout;


public class ContentBuilder {
    public static Content defaultContent() {
        return new Content(ContentLinkBuilder.defaultContentLinks()).supplementLinks(SupplementLinkBuilder.defaultSupplementLinks());
    }

    public static Content defaultContentWithoutSupplementLinks() {
        return new Content(ContentLinkBuilder.defaultContentLinks());
    }
}
