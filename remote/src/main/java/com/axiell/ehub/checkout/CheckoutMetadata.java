package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;

import java.util.Date;

public class CheckoutMetadata {
    private final CheckoutMetadataDTO checkoutDTO;
    private final Format format;

    public CheckoutMetadata(CheckoutMetadataDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
        format = new Format(checkoutDTO.getFormat());
    }

    public Long id() {
        return checkoutDTO.getId();
    }

    public String lmsLoanId() {
        return checkoutDTO.getLmsLoanId();
    }

    public String contentProviderLoanId() {
        return checkoutDTO.getContentProviderLoanId();
    }

    public Date expirationDate() {
        return checkoutDTO.getExpirationDate();
    }

    public Format format() {
        return format;
    }
}
