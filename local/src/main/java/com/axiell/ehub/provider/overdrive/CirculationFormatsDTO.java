package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CirculationFormatsDTO {
    private List<CirculationFormatDTO> formats;
    
    public List<CirculationFormatDTO> getFormats() {
	return formats;
    }
}
