package com.axiell.ehub.provider.overdrive;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Field {
    private String name;
    private String value;

    Field(final String name, final String value) {
	this.name = name;
	this.value = value;
    }
    
    public String getName() {
	return name;
    }
    
    public String getValue() {
	return value;
    }
}
