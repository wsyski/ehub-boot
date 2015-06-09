package com.axiell.ehub.provider.borrowbox;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequestDTO {
    private String recordId;
    private String formatId;

    public CheckoutRequestDTO(final String recordId, final String formatId) {
        this.recordId = recordId;
        this.formatId = formatId;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getFormatId() {
        return formatId;
    }
}
