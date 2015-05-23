package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"id", "type"})

public class DownloadUrlDTO {
    @JsonProperty(value = "url")
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
