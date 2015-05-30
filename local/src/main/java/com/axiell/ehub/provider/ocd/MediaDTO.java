package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"description", "publisher", "narrators", "audience", "genres", "title", "authors", "language", "isbn", "images", "primaryGenre"})
public class MediaDTO {
    private String titleId;
    private String mediaType;

    public String getTitleId() {
        return titleId;
    }

    public String getMediaType() {
        return mediaType;
    }
}
