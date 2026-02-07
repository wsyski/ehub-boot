package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private String id;
    private String crossRefId;
    private String title;
    private List<DiscoveryFormatDTO> formats;

    public String getId() {
        return id;
    }

    public List<DiscoveryFormatDTO> getFormats() {
        return formats;
    }

    public String getCrossRefId() {
        return crossRefId;
    }

    public String getTitle() {
        return title;
    }
}
