package com.axiell.ehub.checkout;

public class Checkout {
    private final CheckoutMetadata metadata;
    private final ContentLink contentLink;

    public Checkout(final CheckoutDTO checkoutDTO) {
        metadata = new CheckoutMetadata(checkoutDTO.getMetadata());
        contentLink = new ContentLink(checkoutDTO.getContentLink());
    }

    public CheckoutMetadata metadata() {
        return metadata;
    }

    public ContentLink contentLink() {
        return contentLink;
    }
}
