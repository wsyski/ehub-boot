package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CirculationFormatsDTO {
    private List<CirculationFormatDTO> formats;
    
    public List<CirculationFormatDTO> getFormats() {
	return formats;
    }
}
