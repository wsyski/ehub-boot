package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"ProductID", "ExpiryDate", "CreatedDate", "Supplements", "CoverImage", "Title"})
public class Loan {
    @JsonProperty("LoanID")
    private String loanId;

    @JsonProperty("Links")
    private Links links;

    @JsonProperty("Active")
    private Boolean active;

    public String getLoanId() {
        return loanId;
    }

    public Boolean isActive() {
        return active;
    }

    String getContentUrlFor(final String formatId) {
        return links.getContentUrlFor(formatId);
    }
}
