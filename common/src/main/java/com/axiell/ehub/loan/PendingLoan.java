/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.axiell.ehub.provider.ContentProviderName;

/**
 * A Pending Loan should be created on the client side and then provided to the eHUB to create a loan both in the LMS
 * and at the content provider.
 */
@XmlRootElement(name = "pendingLoan")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(ContentProviderName.class)
public final class PendingLoan {
    private String lmsRecordId;
    private String contentProviderName;
    private String contentProviderRecordId;
    private String contentProviderFormatId;

    /**
     * Empty constructor required by JAXB.
     */
    protected PendingLoan() {
    }

    /**
     * Constructs a new {@link PendingLoan}.
     * 
     * @param lmsRecordId the ID of the record to loan in the LMS
     * @param contentProviderName the name of the content provider
     * @param contentProviderRecordId the ID of the record to loan at the content provider
     * @param contentProviderFormat the content provider defined format of the record to loan
     */
    public PendingLoan(final String lmsRecordId, final String contentProviderName, final String contentProviderRecordId, final String contentProviderFormat) {
        this.lmsRecordId = lmsRecordId;
        this.contentProviderName = contentProviderName;
        this.contentProviderRecordId = contentProviderRecordId;
        this.contentProviderFormatId = contentProviderFormat;
    }

    /**
     * Returns the ID of the record to loan in the LMS.
     * 
     * @return the ID of the record to loan in the LMS
     */
    @XmlAttribute(name = "lmsRecordId", required = true)
    public String getLmsRecordId() {
        return lmsRecordId;
    }

    /**
     * Sets the ID of the record to loan in the LMS.
     * 
     * @param lmsRecordId the ID of the record to loan in the LMS to set
     */
    protected void setLmsRecordId(String lmsRecordId) {
        this.lmsRecordId = lmsRecordId;
    }

    /**
     * Returns the name of the content provider.
     * 
     * @return the name of the content provider
     */
    @XmlAttribute(name = "contentProviderName", required = true)
    public String getContentProviderName() {
        return contentProviderName;
    }

    /**
     * Sets the name of the content provider.
     * 
     * @param contentProviderName the name of the content provider to set
     */
    protected void setContentProviderName(String contentProviderName) {
        this.contentProviderName = contentProviderName;
    }

    /**
     * Returns the name of the content provider as a {@link ContentProviderName}. This method is not a part of the
     * public API and should only be used internally by the eHUB.
     * 
     * @return a {@link ContentProviderName}
     * @throws
     */
    @XmlTransient
    public final ContentProviderName getContentProviderNameEnum() {
        return ContentProviderName.fromString(contentProviderName);
    }

    /**
     * Returns the ID of the record to loan at the content provider.
     * 
     * @return the ID of the record to loan at the content provider
     */
    @XmlAttribute(name = "contentProviderRecordId", required = true)
    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    /**
     * Sets the ID of the record to loan at the content provider.
     * 
     * @param contentProviderRecordId the ID of the record to loan at the content provider to set
     */
    protected void setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    /**
     * Returns the ID of the format at the content provider the record to loan should have.
     * 
     * @return the ID of the format at the content provider the record to loan should have
     */
    @XmlAttribute(name = "contentProviderFormatId", required = true)
    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    /**
     * Sets the ID of the format at the content provider the record to loan should have.
     * 
     * @param contentProviderFormatId the ID of the format at the content provider the record to loan should have to set
     */
    protected void setContentProviderFormatId(String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }
}
