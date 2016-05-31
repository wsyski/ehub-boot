package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.FormatDTO;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonUnwrapped;

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
