package com.axiell.ehub.controller.external.v5_0.checkout.dto;

import com.axiell.ehub.controller.external.v5_0.provider.dto.FormatDTO;
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
public class CheckoutMetadataDTO implements Serializable {
    private long id;
    private String lmsLoanId;
    private String contentProviderLoanId;
    private Date expirationDate;
    private boolean isNewLoan;
    private String issueId;
    private String issueTitle;
    private FormatDTO format;

    private CheckoutMetadataDTO() {
    }

    public CheckoutMetadataDTO(final long id, final String lmsLoanId, final Date expirationDate, final boolean isNewLoan, final FormatDTO format) {
        this.id = id;
        this.lmsLoanId = lmsLoanId;
        this.expirationDate = new Date(expirationDate.getTime());
        this.isNewLoan = isNewLoan;
        this.format = format;
    }

    public long getId() {
        return id;
    }

    public String getLmsLoanId() {
        return lmsLoanId;
    }

    public String getContentProviderLoanId() {
        return contentProviderLoanId;
    }

    public CheckoutMetadataDTO contentProviderLoanId(final String contentProviderLoanId) {
        this.contentProviderLoanId = contentProviderLoanId;
        return this;
    }

    public Date getExpirationDate() {
        return new Date(expirationDate.getTime());
    }

    public boolean isNewLoan() {
        return isNewLoan;
    }

    public String getIssueId() {
        return issueId;
    }

    public CheckoutMetadataDTO issueId(final String issueId) {
        this.issueId = issueId;
        return this;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public CheckoutMetadataDTO issueTitle(final String issueTitle) {
        this.issueTitle = issueTitle;
        return this;
    }

    public FormatDTO getFormat() {
        return format;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CheckoutMetadataDTO)) {
            return false;
        }
        final CheckoutMetadataDTO rhs = (CheckoutMetadataDTO) obj;
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
