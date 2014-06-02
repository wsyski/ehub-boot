package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@JsonIgnoreProperties(value = {"ProductID", "CreatedDate", "Supplements", "CoverImage", "Active", "Title"})
public class CreatedLoan {
    @JsonProperty("LoanID")
    private String loanId;

    @JsonProperty(value = "ExpiryDate")
    private Date expirationDate;

    @JsonProperty(value = "Links")
    private List<Link> links;

    public String getLoanId() {
        return loanId;
    }

    public Date getExpirationDate() {
        return expirationDate;
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
