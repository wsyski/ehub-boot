package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    @JsonProperty("id")
    private String id;

    @JsonUnwrapped
    private FormatMetadataDTO formatMetadata = new FormatMetadataDTO();

    @JsonProperty("expirationDate")
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public FormatMetadataDTO getFormatMetadata() {
        return formatMetadata;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutDTO id(final String id) {
        this.id = id;
        return this;
    }

    public CheckoutDTO formatMetadata(final FormatMetadataDTO formatMetadata) {
        this.formatMetadata = formatMetadata;
        return this;
    }

    public CheckoutDTO expirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
}
