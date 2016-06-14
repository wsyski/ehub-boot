package com.axiell.ehub.checkout;

import java.io.Serializable;

public class Checkout implements Serializable {
    private final CheckoutDTO checkoutDTO;

    public Checkout(CheckoutMetadata checkoutMetadata, Content content) {
        this.checkoutDTO = new CheckoutDTO().metadata(checkoutMetadata.toDTO()).contentLinks(content.getContentLinks().toDTO()).supplementLinks(content.getSupplementLinks().toDTO());
    }

    public Checkout(final CheckoutDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
    }

    public CheckoutMetadata metadata() {
        return new CheckoutMetadata(checkoutDTO.getMetadata());
    }

    public ContentLinks contentLinks() {
        return ContentLinks.fromDTO(checkoutDTO.getContentLinks());
    }

    public SupplementLinks supplementLinks() {
        return SupplementLinks.fromDTO(checkoutDTO.getSupplementLinks());
    }

    public CheckoutDTO toDTO() {
        return checkoutDTO;
    }
}
