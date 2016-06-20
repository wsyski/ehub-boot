package com.axiell.ehub.provider.overdrive;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private String id;
    private List<DiscoveryFormatDTO> formats;

    public String getId() {
	return id;
    }
    
    public List<DiscoveryFormatDTO> getFormats() {
	return formats;
    }
}
