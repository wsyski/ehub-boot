package com.axiell.ehub.common.checkout;

public class CheckoutBuilder {
    public static Checkout checkout() {
        return new Checkout(CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat(), ContentBuilder.content());
    }

    public static Checkout checkoutWithSupplementalLinks() {
        return new Checkout(CheckoutMetadataBuilder.checkoutMetadataWithDownloadableFormat(), ContentBuilder.contentWithSupplementLinks());
    }
}
