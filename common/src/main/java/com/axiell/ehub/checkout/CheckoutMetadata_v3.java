package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.Format;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class CheckoutMetadata_v3 implements Serializable {
    private final CheckoutMetadataDTO_v3 checkoutMetadataDTO;

    public CheckoutMetadata_v3() {
        this(new CheckoutMetadataDTO_v3());
    }

    public CheckoutMetadata_v3(CheckoutMetadataDTO_v3 checkoutMetadataDTO) {
        this.checkoutMetadataDTO = checkoutMetadataDTO;
    }

    public Long id() {
        return checkoutMetadataDTO.getId();
    }

    public CheckoutMetadata_v3 id(Long id) {
        checkoutMetadataDTO.id(id);
        return this;
    }

    public String lmsLoanId() {
        return checkoutMetadataDTO.getLmsLoanId();
    }

    public CheckoutMetadata_v3 lmsLoanId(String lmsLoanId) {
        checkoutMetadataDTO.lmsLoanId(lmsLoanId);
        return this;
    }

    public String contentProviderLoanId() {
        return checkoutMetadataDTO.getContentProviderLoanId();
    }

    public CheckoutMetadata_v3 contentProviderLoanId(String contentProviderLoanId) {
        checkoutMetadataDTO.contentProviderLoanId(contentProviderLoanId);
        return this;
    }

    public Date expirationDate() {
        return checkoutMetadataDTO.getExpirationDate();
    }

    public CheckoutMetadata_v3 expirationDate(Date expirationDate) {
        checkoutMetadataDTO.expirationDate(expirationDate);
        return this;
    }

    public boolean isNewLoan() {
        return checkoutMetadataDTO.isNewLoan();
    }

    public CheckoutMetadata_v3 newLoan(final boolean newLoan) {
        checkoutMetadataDTO.newLoan(newLoan);
        return this;
    }

    //TODO: Probably we would like to save the issue title in the database and retrieve in My Loans, but this could be done in the next step.
    public String contentProviderIssueTitle() {
        return "title";
    }

    public String contentProviderIssueId() {
        return checkoutMetadataDTO.getContentProviderIssueId();
    }

    public CheckoutMetadata_v3 contentProviderIssueId(String contentProviderIssueId) {
        checkoutMetadataDTO.contentProviderIssueId(contentProviderIssueId);
        return this;
    }

    public Format format() {
        return new Format(checkoutMetadataDTO.getFormat());
    }

    public CheckoutMetadata_v3 format(Format format) {
        checkoutMetadataDTO.format(format.toDTO());
        return this;
    }

    public CheckoutMetadataDTO_v3 toDTO() {
        return checkoutMetadataDTO;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutMetadata_v3)) {
            return false;
        }
        final CheckoutMetadata_v3 rhs = (CheckoutMetadata_v3) obj;
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
