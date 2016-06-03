package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class CheckoutMetadata implements Serializable {
    private final CheckoutMetadataDTO checkoutMetadataDTO;

    public CheckoutMetadata() {
        this(new CheckoutMetadataDTO());
    }

    public CheckoutMetadata(CheckoutMetadataDTO checkoutMetadataDTO) {
        this.checkoutMetadataDTO = checkoutMetadataDTO;
    }

    public Long id() {
        return checkoutMetadataDTO.getId();
    }

    public CheckoutMetadata id(Long id) {
        checkoutMetadataDTO.id(id);
        return this;
    }

    public String lmsLoanId() {
        return checkoutMetadataDTO.getLmsLoanId();
    }

    public CheckoutMetadata lmsLoanId(String lmsLoanId) {
        checkoutMetadataDTO.lmsLoanId(lmsLoanId);
        return this;
    }

    public String contentProviderLoanId() {
        return checkoutMetadataDTO.getContentProviderLoanId();
    }

    public CheckoutMetadata contentProviderLoanId(String contentProviderLoanId) {
        checkoutMetadataDTO.contentProviderLoanId(contentProviderLoanId);
        return this;
    }

    public Date expirationDate() {
        return checkoutMetadataDTO.getExpirationDate();
    }

    public CheckoutMetadata expirationDate(Date expirationDate) {
        checkoutMetadataDTO.expirationDate(expirationDate);
        return this;
    }

    public Format format() {
        return new Format(checkoutMetadataDTO.getFormat());
    }

    public CheckoutMetadata format(Format format) {
        checkoutMetadataDTO.format(format.toDTO());
        return this;
    }

    public CheckoutMetadataDTO toDTO() {
        return checkoutMetadataDTO;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutMetadata)) {
            return false;
        }
        final CheckoutMetadata rhs = (CheckoutMetadata) obj;
        return new EqualsBuilder().append(toDTO(), rhs.toDTO()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(toDTO()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
