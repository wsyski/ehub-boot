/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response from ElibU. 
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Response {
    @JsonProperty("Result")
    private Result result;
    
    /**
     * Returns the result.
     *
     * @return the result
     */
    public Result getResult() {
        return result;
    }
    
    /**
     * Sets the result.
     *
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }
}
