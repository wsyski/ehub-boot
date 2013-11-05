package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
