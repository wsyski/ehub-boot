package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutMetadataDTO {
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
}
