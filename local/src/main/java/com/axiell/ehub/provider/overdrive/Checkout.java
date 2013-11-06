package com.axiell.ehub.provider.overdrive;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.axiell.ehub.util.DateFactory;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Checkout {
    @JsonProperty(value = "expires")
    private Date expirationDate;
    private List<CirculationFormat> formats;
    
    public Date getExpirationDate() {
	return DateFactory.create(expirationDate);
    }
    
    public List<CirculationFormat> getFormats() {
	return formats;
    }
}
