package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class CheckoutMetadata implements Serializable {
    private final CheckoutMetadataDTO checkoutMetadataDTO;

    public CheckoutMetadata(final long id, final String lmsLoanId, final Date expirationDate, final boolean isNewLoan, final Format format) {
        this.checkoutMetadataDTO = new CheckoutMetadataDTO(id, lmsLoanId, expirationDate, isNewLoan, format.toDTO());
    }

    public CheckoutMetadata(CheckoutMetadataDTO checkoutMetadataDTO) {
        this.checkoutMetadataDTO = checkoutMetadataDTO;
    }

    public long getId() {
        return checkoutMetadataDTO.getId();
    }

    public String getLmsLoanId() {
        return checkoutMetadataDTO.getLmsLoanId();
    }

    public String getContentProviderLoanId() {
        return checkoutMetadataDTO.getContentProviderLoanId();
    }

    public CheckoutMetadata contentProviderLoanId(final String contentProviderLoanId) {
        checkoutMetadataDTO.contentProviderLoanId(contentProviderLoanId);
        return this;
    }

    public Date getExpirationDate() {
        return checkoutMetadataDTO.getExpirationDate();
    }

    public boolean isNewLoan() {
        return checkoutMetadataDTO.isNewLoan();
    }

    public String getContentProviderIssueId() {
        return checkoutMetadataDTO.getContentProviderIssueId();
    }

    public CheckoutMetadata contentProviderIssueId(final String contentProviderIssueId) {
        checkoutMetadataDTO.contentProviderIssueId(contentProviderIssueId);
        return this;
    }

    public Format getFormat() {
        return new Format(checkoutMetadataDTO.getFormat());
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
