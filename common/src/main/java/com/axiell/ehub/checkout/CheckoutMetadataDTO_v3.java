package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutMetadataDTO_v3 implements Serializable {
    private Long id;
    private String lmsLoanId;
    private String contentProviderLoanId;
    private Date expirationDate;
    private boolean newLoan;
    private String contentProviderIssueId;
    private FormatDTO format;

    public Long getId() {
        return id;
    }

    public CheckoutMetadataDTO_v3 id(Long id) {
        this.id = id;
        return this;
    }

    public String getLmsLoanId() {
        return lmsLoanId;
    }

    public CheckoutMetadataDTO_v3 lmsLoanId(String lmsLoanId) {
        this.lmsLoanId = lmsLoanId;
        return this;
    }

    public String getContentProviderLoanId() {
        return contentProviderLoanId;
    }

    public CheckoutMetadataDTO_v3 contentProviderLoanId(String contentProviderLoanId) {
        this.contentProviderLoanId = contentProviderLoanId;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutMetadataDTO_v3 expirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public boolean isNewLoan() {
        return newLoan;
    }

    public CheckoutMetadataDTO_v3 newLoan(final boolean newLoan) {
        this.newLoan = newLoan;
        return this;
    }

    public String getContentProviderIssueId() {
        return contentProviderIssueId;
    }

    public CheckoutMetadataDTO_v3 contentProviderIssueId(final String contentProviderIssueId) {
        this.contentProviderIssueId = contentProviderIssueId;
        return this;
    }

    public FormatDTO getFormat() {
        return format;
    }

    public CheckoutMetadataDTO_v3 format(FormatDTO formatDTO) {
        this.format = formatDTO;
        return this;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutMetadataDTO_v3)) {
            return false;
        }
        final CheckoutMetadataDTO_v3 rhs = (CheckoutMetadataDTO_v3) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
