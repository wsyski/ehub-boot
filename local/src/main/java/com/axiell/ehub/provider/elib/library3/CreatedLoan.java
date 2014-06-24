package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"CreatedDate", "Supplements", "CoverImage", "Active", "Title"})
public class CreatedLoan {
    @JsonProperty("ProductID")
    private String productId;
    @JsonProperty("LoanID")
    private String loanId;

    public String getProductId() {
        return productId;
    }

    @JsonProperty(value = "ExpiryDate")
    private Date expirationDate;

    @JsonProperty(value = "Links")
    private Links links;

    public String getLoanId() {
        return loanId;
    }

    public Date getExpirationDate() {
        return new Date(expirationDate.getTime());
    }

    String getContentUrlFor(String formatId) {
        return links.getContentUrlFor(formatId);
    }
}
