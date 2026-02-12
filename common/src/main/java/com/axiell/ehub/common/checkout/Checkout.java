package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutDTO;

import java.io.Serializable;

public class Checkout implements Serializable {
    private final CheckoutDTO checkoutDTO;

    public Checkout(final CheckoutMetadata checkoutMetadata, final Content content) {
        this.checkoutDTO = new CheckoutDTO(checkoutMetadata.toDTO(), content.getContentLinks().toDTO()).supplementLinks(content.getSupplementLinks().toDTO());
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
