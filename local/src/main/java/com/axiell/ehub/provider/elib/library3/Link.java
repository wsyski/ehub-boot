package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Iterator;
import java.util.List;

public class Link {
    @JsonProperty(value = "FormatID")
    private String formatId;
    @JsonProperty(value = "Contents")
    private List<Content> contents;

    boolean isLinkForFormat(final String formatIdInRequest) {
        return formatId.equals(formatIdInRequest);
    }

    String getFirstContentUrl() {
        if (contents == null)
            return null;
        final Iterator<Content> itr = contents.iterator();
        final Content firstContent = itr.hasNext() ? itr.next() : null;
        return firstContent == null ? null : firstContent.getUrl();
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
