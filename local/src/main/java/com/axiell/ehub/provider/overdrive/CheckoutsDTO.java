package com.axiell.ehub.provider.overdrive;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutsDTO {
    private List<CheckoutDTO> checkouts;
    
    public List<CheckoutDTO> getCheckouts() {
	return checkouts;
    }
}
