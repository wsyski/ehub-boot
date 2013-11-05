package com.axiell.ehub.provider.overdrive;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Checkout {
    @JsonProperty(value = "expires")
    private Date expirationDate;
    private List<CirculationFormat> formats;
    
    public Date getExpirationDate() {
	return expirationDate;
    }
    
    public List<CirculationFormat> getFormats() {
	return formats;
    }
}
