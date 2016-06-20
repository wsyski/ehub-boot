package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadLinkDTO {
    private Links links;

    public Links getLinks() {
        return links;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        @JsonProperty(value = "contentlink")
        private ContentLink contentLink;

        public ContentLink getContentLink() {
            return contentLink;
        }

        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ContentLink {
            private String href;

            public String getHref() {
                return href;
            }
        }
    }
}
