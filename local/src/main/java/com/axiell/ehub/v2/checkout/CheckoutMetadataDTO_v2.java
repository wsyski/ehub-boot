package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.axiell.ehub.v2.provider.record.format.FormatDTO_v2;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CheckoutMetadataDTO_v2 {
    private Long id;
    private String lmsLoanId;
    private String contentProviderLoanId;
    private Date expirationDate;
    private FormatDTO_v2 format;

    static CheckoutMetadataDTO_v2 fromDTO(final CheckoutMetadataDTO checkoutMetadataDTO) {
        FormatDTO formatDTO = checkoutMetadataDTO.getFormat();
        FormatDTO_v2 formatDTO_v2 = new FormatDTO_v2().id(formatDTO.getId()).contentDisposition(formatDTO.getContentDisposition())
                .description(formatDTO.getDescription()).name(formatDTO.getName()).playerWidth(0).playerHeight(0);
        return new CheckoutMetadataDTO_v2().id(checkoutMetadataDTO.getId()).lmsLoanId(checkoutMetadataDTO.getLmsLoanId())
                .contentProviderLoanId(checkoutMetadataDTO.getContentProviderLoanId()).expirationDate(checkoutMetadataDTO.getExpirationDate())
                .format(formatDTO_v2);
    }

    public Long getId() {
        return id;
    }

    public CheckoutMetadataDTO_v2 id(Long id) {
        this.id = id;
        return this;
    }

    public String getLmsLoanId() {
        return lmsLoanId;
    }

    public CheckoutMetadataDTO_v2 lmsLoanId(String lmsLoanId) {
        this.lmsLoanId = lmsLoanId;
        return this;
    }

    public String getContentProviderLoanId() {
        return contentProviderLoanId;
    }

    public CheckoutMetadataDTO_v2 contentProviderLoanId(String contentProviderLoanId) {
        this.contentProviderLoanId = contentProviderLoanId;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutMetadataDTO_v2 expirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public FormatDTO_v2 getFormat() {
        return format;
    }

    public CheckoutMetadataDTO_v2 format(final FormatDTO_v2 formatDTO) {
        this.format = formatDTO;
        return this;
    }
}
