package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("ResponseStatus")
    private String status;

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
