package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"id", "type"})

public class DownloadUrlDTO {
    @JsonProperty(value = "url")
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
