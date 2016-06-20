package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaDTO {
    private String isbn;
    private String mediaType;

    public String getIsbn() {
        return isbn;
    }

    public String getMediaType() {
        return mediaType;
    }
}
