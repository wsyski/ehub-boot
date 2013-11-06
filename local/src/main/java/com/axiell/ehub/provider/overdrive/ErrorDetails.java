package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {
    private String message;
    private String token;
    
    public ErrorDetails() {	
    }
    
    public ErrorDetails(String message) {
	this.message = message;
    }
    
    public String getMessage() {
	return message;
    }
    
    public String getToken() {
	return token;
    }
}
