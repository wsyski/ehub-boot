package com.axiell.ehub.provider.borrowbox;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String message;
    private String errorCode;

    public ErrorDTO() {
    }

    public ErrorDTO(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
