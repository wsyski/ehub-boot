package com.axiell.ehub.provider.ep;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;
    private String contentUrl;
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
