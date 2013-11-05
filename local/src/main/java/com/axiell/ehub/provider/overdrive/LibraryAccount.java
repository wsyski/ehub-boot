package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryAccount {
    private String collectionToken;
    
    public String getCollectionToken() {
	return collectionToken;
    }
}
