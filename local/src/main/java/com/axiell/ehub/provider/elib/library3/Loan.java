package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(value = {"CreatedDate", "Supplements", "CoverImage", "Title"})
public class Loan {
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

    public String getLoanId() {
        return loanId;
    }

    public String getProductId() {
        return productId;
    }

    public Date getExpirationDate() {
        return new Date(expirationDate.getTime());
    }

    public Boolean isActive() {
        return active;
    }

    String getContentUrlFor(final String formatId) {
        return links.getContentUrlFor(formatId);
    }
}
