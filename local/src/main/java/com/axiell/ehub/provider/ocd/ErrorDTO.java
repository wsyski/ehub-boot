package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String message;

    public ErrorDTO() {
    }
    
    public ErrorDTO(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return message;
    }
}
