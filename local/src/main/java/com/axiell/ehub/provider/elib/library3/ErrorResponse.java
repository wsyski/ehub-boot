package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class ErrorResponse {
    @JsonProperty("Reason")
    private String reason;

    public String getReason() {
        return reason;
    }
}
