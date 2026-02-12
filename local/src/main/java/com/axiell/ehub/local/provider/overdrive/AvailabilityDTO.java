package com.axiell.ehub.local.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
