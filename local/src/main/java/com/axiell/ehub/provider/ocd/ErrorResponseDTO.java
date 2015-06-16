package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponseDTO {
    private String message;

    public ErrorResponseDTO() {
    }
    
    public ErrorResponseDTO(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return message;
    }
}
