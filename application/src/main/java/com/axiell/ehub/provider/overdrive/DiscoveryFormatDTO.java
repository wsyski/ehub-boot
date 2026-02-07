package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryFormatDTO {
    private String id;
    private String name;
    
    public String getId() {
	return id;
    }
    
    public String getName() {
	return name;
    }
}
