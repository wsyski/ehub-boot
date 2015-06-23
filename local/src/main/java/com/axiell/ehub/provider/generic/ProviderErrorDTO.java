package com.axiell.ehub.provider.generic;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderErrorDTO {
    private String message;
    private String cause;

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }
}

