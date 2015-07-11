package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect
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
