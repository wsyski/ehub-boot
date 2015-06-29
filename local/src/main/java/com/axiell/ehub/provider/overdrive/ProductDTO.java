package com.axiell.ehub.provider.overdrive;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private String id;
    private List<DiscoveryFormat> formats;

    public String getId() {
	return id;
    }
    
    public List<DiscoveryFormat> getFormats() {
	return formats;
    }
}
