package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Iterator;
import java.util.List;

@JsonIgnoreProperties(value = {"FormatID"})
public class Link {
    @JsonProperty(value = "Contents")
    private List<Content> contents;

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
