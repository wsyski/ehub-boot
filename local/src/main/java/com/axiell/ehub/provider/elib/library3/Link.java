package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class Link {
    @JsonProperty(value = "FormatID")
    private String formatId;
    @JsonProperty(value = "Contents")
    private List<Content> contents;

    boolean isLinkForFormat(final String formatIdInRequest) {
        return formatId.equals(formatIdInRequest);
    }

    List<String> getContentUrls() {
        if (contents == null)
            return null;
        return contents.stream().map(Content::getUrl).collect(Collectors.toList());
    }

    @JsonIgnoreProperties(value = {"Part"})
    public static class Content {
        @JsonProperty(value = "Url")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
