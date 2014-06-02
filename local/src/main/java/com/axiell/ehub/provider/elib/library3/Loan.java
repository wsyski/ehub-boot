package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Iterator;
import java.util.List;

@JsonIgnoreProperties(value = {"ProductID", "ExpiryDate", "CreatedDate", "Supplements", "CoverImage", "Title"})
public class Loan {
    @JsonProperty("LoanID")
    private String loanId;

    @JsonProperty("Links")
    private List<Link> links;

    @JsonProperty("Active")
    private Boolean active;

    public String getLoanId() {
        return loanId;
    }

    public Boolean isActive() {
        return active;
    }

    String getFirstContentUrl() {
        final Link firstLink = getFirstLink();
        return firstLink == null ? null : firstLink.getFirstContentUrl();
    }

    private Link getFirstLink() {
        if (links == null)
            return null;
        final Iterator<Link> itr = links.iterator();
        return itr.hasNext() ? itr.next() : null;
    }


}
