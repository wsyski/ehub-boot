package com.axiell.ehub.provider.ep.lpp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class CheckoutRequestDTO {
    @JsonProperty(value = "recordId")
    private String recordId;

    public CheckoutRequestDTO(final String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;

    }
}

