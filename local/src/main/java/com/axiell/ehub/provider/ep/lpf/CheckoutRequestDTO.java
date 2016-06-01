package com.axiell.ehub.provider.ep.lpf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequestDTO {
    private String recordId;
    private String formatId;

    public CheckoutRequestDTO(final String recordId, final String formatId) {
        this.recordId = recordId;
        this.formatId = formatId;
    }

    private CheckoutRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }

    public String getFormatId() {
        return formatId;
    }
}
