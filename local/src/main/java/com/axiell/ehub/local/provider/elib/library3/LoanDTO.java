package com.axiell.ehub.local.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"CreatedDate", "CoverImage", "Title"})
public class LoanDTO {
    @JsonProperty("LoanID")
    private String loanId;

    @JsonProperty("ProductID")
    private String productId;

    @JsonProperty("ExpiryDate")
    private Date expirationDate;

    @JsonProperty("Links")
    private Links links;

    @JsonProperty("Active")
    private Boolean active;

    @JsonProperty("Supplements")
    private Supplements supplements;

    public String getLoanId() {
        return loanId;
    }

    public String getProductId() {
        return productId;
    }

    public Date getExpirationDate() {
        return new Date(expirationDate.getTime());
    }

    public Supplements getSupplements() {
        return supplements;
    }

    public Boolean isActive() {
        return active;
    }

    public List<String> getContentUrlsFor(final String formatId) {
        return links.getContentUrlsFor(formatId);
    }
}
