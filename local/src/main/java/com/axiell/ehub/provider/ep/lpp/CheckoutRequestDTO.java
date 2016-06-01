package com.axiell.ehub.provider.ep.lpp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequestDTO {
    private String recordId;

    public CheckoutRequestDTO(final String recordId) {
        this.recordId = recordId;
    }

    private CheckoutRequestDTO() {
    }

    public String getRecordId() {
        return recordId;
    }
}

