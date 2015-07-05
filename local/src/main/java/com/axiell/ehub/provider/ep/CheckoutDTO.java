package com.axiell.ehub.provider.ep;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String loanId;
    private String contentUrl;
    @JsonProperty(value = "endDate")
    private Date expirationDate;

    public String getLoanId() {
        return loanId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
