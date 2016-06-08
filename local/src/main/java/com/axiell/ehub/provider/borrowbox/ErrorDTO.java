package com.axiell.ehub.provider.borrowbox;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String message;
    private String errorCode;

    public String getMessage() {
	return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
