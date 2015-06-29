package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityDTO {
    private String id;
    private boolean available;

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }
}
