/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the status of the {@link Response}.
 */
@JsonIgnoreProperties(value = {"Message"})
public class Status {
    private static final int OK = 0;
    private static final int LICENSE_ALREADY_EXISTS = 51;
    private static final int LICENSE_ALREADY_CONSUMED_PRODUCT = 52;
    
    @JsonProperty("Code")
    private Integer code;
    
    /**
     * Returns the status code.
     *
     * @return the status code
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * Sets the status code.
     *
     * @param code the status code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
    /**
     * Indicates whether the license was successfully consumed.
     * 
     * @return <code>true</code> if and only if the license was successfully consumed, <code>false</code> otherwise
     */
    public boolean isConsumedLicense() {
        return code == OK || code == LICENSE_ALREADY_EXISTS;
    }
    
    /**
     * Indicates whether the product was successfully consumed.
     * 
     * @return <code>true</code> if and only if the product was successfully consumed, <code>false</code> otherwise
     */
    public boolean isConsumedProduct() {
        return code == OK || code == LICENSE_ALREADY_CONSUMED_PRODUCT;
    }
    
    /**
     * Indicates whether the products was successfully retrieved.
     * 
     * @return <code>true</code> if and only if the product was successfully retrieved, <code>false</code> otherwise
     */
    public boolean hasRetrievedProduct() {
        return code == OK;
    }
}
