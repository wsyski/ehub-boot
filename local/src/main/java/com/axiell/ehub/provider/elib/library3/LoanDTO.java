package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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

    List<String> getContentUrlsFor(final String formatId) {
        return links.getContentUrlsFor(formatId);
    }
}
