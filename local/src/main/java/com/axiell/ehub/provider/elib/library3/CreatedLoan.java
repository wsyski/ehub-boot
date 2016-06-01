package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"CreatedDate", "CoverImage", "Active", "Title"})
public class CreatedLoan {
    @JsonProperty("ProductID")
    private String productId;
    @JsonProperty("LoanID")
    private String loanId;
    @JsonProperty(value = "ExpiryDate")
    private Date expirationDate;
    @JsonProperty(value = "Links")
    private Links links;
    @JsonProperty("Supplements")
    private Supplements supplements;

    public String getProductId() {
        return productId;
    }

    public String getLoanId() {
        return loanId;
    }

    public Supplements getSupplements() {
        return supplements;
    }

    public Date getExpirationDate() {
        return new Date(expirationDate.getTime());
    }

    List<String> getContentUrlsFor(String formatId) {
        return links.getContentUrlsFor(formatId);
    }
}
