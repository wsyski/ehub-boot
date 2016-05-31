package com.axiell.ehub.provider.ep.lpp;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

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

