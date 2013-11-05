package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadLink {
    private Links links;
    
    public Links getLinks() {
	return links;
    }
    
    @JsonAutoDetect
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
	@JsonProperty(value = "contentlink")
	private ContentLink contentLink;
	
	public ContentLink getContentLink() {
	    return contentLink;
	}
	
	@JsonAutoDetect
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ContentLink {
	    private String href;
	    
	    public String getHref() {
		return href;
	    }
	}
    }
}
