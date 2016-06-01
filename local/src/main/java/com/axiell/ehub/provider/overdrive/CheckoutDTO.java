package com.axiell.ehub.provider.overdrive;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.axiell.ehub.util.DateFactory;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String reserveId;
    @JsonProperty(value = "expires")
    private Date expirationDate;
    private boolean isFormatLocked;

    private List<CirculationFormatDTO> formats;

    public String getReserveId() {
        return reserveId;
    }

    public Date getExpirationDate() {
	return DateFactory.create(expirationDate);
    }
    
    public List<CirculationFormatDTO> getFormats() {
	return formats;
    }

    public boolean isFormatLocked() {
        return isFormatLocked;
    }
}
