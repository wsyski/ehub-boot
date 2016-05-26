package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;

import java.io.Serializable;
import java.util.Date;

public class CheckoutMetadata implements Serializable {
    private final CheckoutMetadataDTO checkoutDTO;

    public CheckoutMetadata() {
        this(new CheckoutMetadataDTO());
    }

    public CheckoutMetadata(CheckoutMetadataDTO checkoutDTO) {
        this.checkoutDTO = checkoutDTO;
    }

    public Long id() {
        return checkoutDTO.getId();
    }

    public CheckoutMetadata id(Long id) {
        checkoutDTO.id(id);
        return this;
    }

    public String lmsLoanId() {
        return checkoutDTO.getLmsLoanId();
    }

    public CheckoutMetadata lmsLoanId(String lmsLoanId) {
        checkoutDTO.lmsLoanId(lmsLoanId);
        return this;
    }

    public String contentProviderLoanId() {
        return checkoutDTO.getContentProviderLoanId();
    }

    public CheckoutMetadata contentProviderLoanId(String contentProviderLoanId) {
        checkoutDTO.contentProviderLoanId(contentProviderLoanId);
        return this;
    }

    public Date expirationDate() {
        return checkoutDTO.getExpirationDate();
    }

    public CheckoutMetadata expirationDate(Date expirationDate) {
        checkoutDTO.expirationDate(expirationDate);
        return this;
    }

    public Format format() {
        return new Format(checkoutDTO.getFormat());
    }

    public CheckoutMetadata format(Format format) {
        checkoutDTO.format(format.toDTO());
        return this;
    }

    public CheckoutMetadataDTO toDTO() {
        return checkoutDTO;
    }
}
