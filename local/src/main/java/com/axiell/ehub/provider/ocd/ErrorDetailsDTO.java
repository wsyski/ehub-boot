package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetailsDTO {
    private String message;

    public ErrorDetailsDTO() {
    }
    
    public ErrorDetailsDTO(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return message;
    }
}
