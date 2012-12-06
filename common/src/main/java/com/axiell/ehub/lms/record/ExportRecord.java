/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.record;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.apache.commons.lang3.Validate;

/**
 * Represents a record exported from an LMS.
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class ExportRecord {
    private String lmsRecordId;
    private Set<URL> externalUrls = new HashSet<>();

    /**
     * Empty constructor required by JAXB.
     */
    protected ExportRecord() {
    }
    
    /**
     * Constructs a new {@link ExportRecord}.
     * 
     * @param lmsRecordId the ID of the record in the LMS
     */
    public ExportRecord(final String lmsRecordId) {
        this.lmsRecordId = lmsRecordId;
    }
    
    /**
     * Returns the ID of the record in the LMS.
     *
     * @return the ID of the record in the LMS
     */
    @XmlAttribute(name = "lmsRecordId", required = true)
    public String getLmsRecordId() {
        return lmsRecordId;
    }
    
    /**
     * Sets the ID of the record in the LMS.
     *
     * @param lmsRecordId the ID of the record in the LMS to set
     */
    protected void setLmsRecordId(final String lmsRecordId) {
        this.lmsRecordId = lmsRecordId;
    }
    
    /**
     * Returns the external {@link URL}s.
     *
     * @return the external {@link URL}s
     */
    @XmlElementWrapper(name = "externalUrls")
    @XmlElement(name = "externalUrl", required = true)
    public Set<URL> getExternalUrls() {
        return externalUrls;
    }
    
    /**
     * Sets the external {@link URL}s.
     *
     * @param externalUrls the external {@link URL}s to set
     */
    protected void setExternalUrls(final Set<URL> externalUrls) {
        this.externalUrls = externalUrls;
    }
    
    /**
     * Adds an external {@link URL} to the underlying {@link Set} of {@link URL}s.
     * 
     * @param externalUrl the external {@link URL} to add
     * @throws NullPointerException if the provided {@link URL} is null
     */
    public void addExternalUrl(final URL externalUrl) {
        Validate.notNull(externalUrl, "Can't add an external URL that is null");
        externalUrls.add(externalUrl);
    }
}
