package com.axiell.ehub.checkout;

public class Checkout {
    private final CheckoutDTO checkoutDTO;

    public Checkout(CheckoutMetadata checkoutMetadata, ContentLink contentLink) {
        this.checkoutDTO = new CheckoutDTO().metadata(checkoutMetadata.toDTO()).contentLink(contentLink.toDTO());
    }

    public Checkout(final CheckoutDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
    }

    public CheckoutMetadata metadata() {
        return new CheckoutMetadata(checkoutDTO.getMetadata());
    }

    public ContentLink contentLink() {
        return new ContentLink(checkoutDTO.getContentLink());
    }

    CheckoutDTO toDTO() {
        return checkoutDTO;
    }
}
