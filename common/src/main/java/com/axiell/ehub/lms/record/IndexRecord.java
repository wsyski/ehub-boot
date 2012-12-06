/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.record;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.axiell.ehub.provider.ContentProvider;

/**
 * Represents a record to be indexed in an LMS (or its clients).
 * 
 * <p>
 * Instances of this class always contains the ID of the record in the LMS. If the record in the LMS has a corresponding
 * record at a {@link ContentProvider} supported by the eHUB this class also contains the ID of the record at the
 * {@link ContentProvider} and the name of the {@link ContentProvider}.
 * </p>
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class IndexRecord {
    private String lmsRecordId;
    private String contentProviderRecordId;
    private String contentProviderName;

    /**
     * Empty constructor required by JAXB.
     */
    protected IndexRecord() {
    }

    /**
     * Constructs a new {@link IndexRecord}.
     * 
     * @param lmsRecordId the ID of the record in the LMS
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider}, <code>null</code> if this
     * record does not exist at any of the {@link ContentProvider}s supported by the eHUB
     * @param contentProviderName the name of the {@link ContentProvider} where this record exist, <code>null</code> if
     * this record does not exist at any of the {@link ContentProvider}s supported by the eHUB
     */
    public IndexRecord(final String lmsRecordId, final String contentProviderRecordId, final String contentProviderName) {
        this.lmsRecordId = lmsRecordId;
        this.contentProviderRecordId = contentProviderRecordId;
        this.contentProviderName = contentProviderName;
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
    protected void setLmsRecordId(String lmsRecordId) {
        this.lmsRecordId = lmsRecordId;
    }

    /**
     * Returns the ID of the record at the {@link ContentProvider}.
     * 
     * @return the ID of the record at the {@link ContentProvider}, can be <code>null</code>
     */
    @XmlAttribute(name = "contentProviderRecordId", required = false)
    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    /**
     * Sets the ID of the record at the {@link ContentProvider}.
     * 
     * @param contentProviderRecordId the ID of the record at the {@link ContentProvider} to set
     */
    protected void setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    /**
     * Returns the name of the {@link ContentProvider}.
     * 
     * @return the name of the {@link ContentProvider}, can be <code>null</code>
     */
    @XmlAttribute(name = "contentProviderName", required = false)
    public String getContentProviderName() {
        return contentProviderName;
    }

    /**
     * Sets the name of the {@link ContentProvider}.
     * 
     * @param contentProviderName the name of the {@link ContentProvider} to set
     */
    protected void setContentProviderName(String contentProviderName) {
        this.contentProviderName = contentProviderName;
    }

    /**
     * Convenience method that indicates whether this {@link IndexRecord} represents a record at a
     * {@link ContentProvider}.
     * 
     * @return <code>true</code> if and only if this {@link IndexRecord} represents a record at a
     * {@link ContentProvider}, <code>false</code> otherwise
     */
    public boolean isContentProviderRecord() {
        return contentProviderRecordId != null && contentProviderName != null;
    }
}
