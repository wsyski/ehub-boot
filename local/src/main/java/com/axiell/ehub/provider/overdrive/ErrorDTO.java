package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String errorCode;
    private String error;
    private String message;
    private String error_description;
    private String token;

    public String getErrorCode() {
        return errorCode == null ? error : errorCode;
    }

    public String getMessage() {
        return message==null ? error_description  : message;
    }
}
