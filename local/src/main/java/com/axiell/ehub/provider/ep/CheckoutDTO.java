package com.axiell.ehub.provider.ep;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;
    private List<String> contentUrls;
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public List<String> getContentUrls() {
        return contentUrls;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
