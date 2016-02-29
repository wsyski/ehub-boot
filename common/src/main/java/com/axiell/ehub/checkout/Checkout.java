package com.axiell.ehub.checkout;

public class Checkout {
    private final CheckoutDTO checkoutDTO;

    public Checkout(CheckoutMetadata checkoutMetadata, ContentLinks contentLinks) {
        this.checkoutDTO = new CheckoutDTO().metadata(checkoutMetadata.toDTO()).contentLinks(contentLinks.toDTO());
    }

    public Checkout(final CheckoutDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
    }

    public CheckoutMetadata metadata() {
        return new CheckoutMetadata(checkoutDTO.getMetadata());
    }

    public ContentLinks contentLinks() {
        return new ContentLinks(checkoutDTO.getContentLinks());
    }

    public CheckoutDTO toDTO() {
        return checkoutDTO;
    }
}
