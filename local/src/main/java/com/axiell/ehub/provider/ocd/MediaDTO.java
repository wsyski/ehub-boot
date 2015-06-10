package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
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
