package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
