package com.axiell.ehub.provider.overdrive;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutsDTO {
    private List<CheckoutDTO> checkouts;
    
    public List<CheckoutDTO> getCheckouts() {
	return checkouts;
    }
}
