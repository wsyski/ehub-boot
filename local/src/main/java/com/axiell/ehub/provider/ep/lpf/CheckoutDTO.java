package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.checkout.SupplementLinkDTO;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonUnwrapped;

import java.util.Date;
import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;

    @JsonUnwrapped
    private FormatMetadataDTO formatMetadata = new FormatMetadataDTO();

    private Date expirationDate;

    public String getId() {
        return id;
    }

    public List<ContentLinkDTO> getContentLinks() {
        return formatMetadata.getContentLinks();
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return formatMetadata.getSupplementLinks();
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CheckoutDTO id(final String id) {
        this.id = id;
        return this;
    }

    public CheckoutDTO formatMetadata(final FormatMetadataDTO formatMetadata) {
        this.formatMetadata = formatMetadata;
        return this;
    }

    public CheckoutDTO expirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
}
