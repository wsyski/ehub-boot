package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.checkout.SupplementLinkDTO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;
    private List<ContentLinkDTO> contentLinks;
    private List<SupplementLinkDTO> supplementLinks;
    private Date expirationDate;

    public String getId() {
        return id;
    }

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks;
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return supplementLinks;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
