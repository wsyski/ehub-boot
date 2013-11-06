package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryFormat {
    private String id;
    private String name;
    
    public String getId() {
	return id;
    }
    
    public String getName() {
	return name;
    }
}
