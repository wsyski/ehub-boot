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
public class CheckoutMetadataDTO implements Serializable {
    private Long id;
    private String lmsLoanId;
    private String contentProviderLoanId;
    private Date expirationDate;
    private FormatDTO format;

    public Long getId() {
        return id;
    }

    public CheckoutMetadataDTO id(Long id) {
        this.id = id;
        return this;
    }

    public String getLmsLoanId() {
        return lmsLoanId;
    }

    public CheckoutMetadataDTO lmsLoanId(String lmsLoanId) {
        this.lmsLoanId = lmsLoanId;
        return this;
    }

    public String getContentProviderLoanId() {
        return contentProviderLoanId;
    }

    public CheckoutMetadataDTO contentProviderLoanId(String contentProviderLoanId) {
        this.contentProviderLoanId = contentProviderLoanId;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutMetadataDTO expirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public FormatDTO getFormat() {
        return format;
    }

    public CheckoutMetadataDTO format(FormatDTO formatDTO) {
        this.format = formatDTO;
        return this;
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
