/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a license, i.e. uniquely one client for at least one term.
 */
@JsonIgnoreProperties(value = {"SubscriptionID"})
public class License {
    @JsonProperty("LicenseID")
    private Integer licenseId;
    
    /**
     * Returns the ID of the license.
     *
     * @return the ID of the license
     */    
    public Integer getLicenseId() {
        return licenseId;
    }
    
    /**
     * Sets the ID of the license.
     *
     * @param licenseId the ID of the license to set
     */
    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }
}
