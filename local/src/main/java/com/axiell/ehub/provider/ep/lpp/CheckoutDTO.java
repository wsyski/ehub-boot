package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.FormatDTO;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Date;
import java.util.Map;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String id;

    @JsonUnwrapped
    private Map<FormatDTO, FormatMetadataDTO> formatMetadata;

    private Date expirationDate;

    public String getId() {
        return id;
    }

    public Map<FormatDTO, FormatMetadataDTO> getFormatMetadata() {
        return formatMetadata;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
