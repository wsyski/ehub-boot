package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthAccessToken {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    public String getAccessToken() {
	return accessToken;
    }
    
    public String getTokenType() {
	return tokenType;
    }
    
    @Override
    public String toString() {
       return tokenType + " " + accessToken;
    }
}
