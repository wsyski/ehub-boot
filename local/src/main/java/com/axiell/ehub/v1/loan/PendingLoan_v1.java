/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.ehub.provider.ContentProviderName;

import javax.xml.bind.annotation.*;

/**
 * A Pending Loan should be created on the client side and then provided to the eHUB to create a loan both in the LMS
 * and at the contentLink provider.
 */
@XmlRootElement(name = "pendingLoan")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(ContentProviderName.class)
public class PendingLoan_v1 {
    private String lmsRecordId;
    private String contentProviderName;
    private String contentProviderRecordId;
    private String contentProviderFormatId;

    /**
     * Empty constructor required by JAXB.
     */
    protected PendingLoan_v1() {
    }

    /**
     * Constructs a new {@link PendingLoan_v1}.
     *
     * @param lmsRecordId             the ID of the record to loan in the LMS
     * @param contentProviderName     the name of the contentLink provider
     * @param contentProviderRecordId the ID of the record to loan at the contentLink provider
     * @param contentProviderFormat   the contentLink provider defined format of the record to loan
     */
    public PendingLoan_v1(final String lmsRecordId, final String contentProviderName, final String contentProviderRecordId, final String contentProviderFormat) {
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
     * Returns the name of the contentLink provider.
     *
     * @return the name of the contentLink provider
     */
    @XmlAttribute(name = "contentProviderName", required = true)
    public String getContentProviderName() {
        return contentProviderName;
    }

    /**
     * Sets the name of the contentLink provider.
     *
     * @param contentProviderName the name of the contentLink provider to set
     */
    protected void setContentProviderName(String contentProviderName) {
        this.contentProviderName = contentProviderName;
    }

    /**
     * Returns the ID of the record to loan at the contentLink provider.
     *
     * @return the ID of the record to loan at the contentLink provider
     */
    @XmlAttribute(name = "contentProviderRecordId", required = true)
    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    /**
     * Sets the ID of the record to loan at the contentLink provider.
     *
     * @param contentProviderRecordId the ID of the record to loan at the contentLink provider to set
     */
    protected void setContentProviderRecordId(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    /**
     * Returns the ID of the format at the contentLink provider the record to loan should have.
     *
     * @return the ID of the format at the contentLink provider the record to loan should have
     */
    @XmlAttribute(name = "contentProviderFormatId", required = true)
    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }

    /**
     * Sets the ID of the format at the contentLink provider the record to loan should have.
     *
     * @param contentProviderFormatId the ID of the format at the contentLink provider the record to loan should have to set
     */
    protected void setContentProviderFormatId(String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }
}
