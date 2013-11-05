package com.axiell.ehub.provider.overdrive;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Checkouts {
    private List<Checkout> checkouts;
    
    public List<Checkout> getCheckouts() {
	return checkouts;
    }
}
