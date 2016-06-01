package com.axiell.ehub.provider.ep.lpf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class CheckoutRequestDTO {
    @JsonProperty(value = "recordId")
    private String recordId;
    @JsonProperty(value = "formatId")
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
