/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.provider.ContentProviderName;

public class PendingLoan {
    private String lmsRecordId;
    private String contentProviderName;
    private String contentProviderRecordId;
    private String contentProviderFormatId;

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
    public String getLmsRecordId() {
        return lmsRecordId;
    }

    /**
     * Returns the name of the getContent provider.
     *
     * @return the name of the getContent provider
     */
    public String getContentProviderName() {
        return contentProviderName;
    }

    /**
     * Returns the ID of the record to loan at the getContent provider.
     *
     * @return the ID of the record to loan at the getContent provider
     */
    public String getContentProviderRecordId() {
        return contentProviderRecordId;
    }

    /**
     * Returns the ID of the format at the getContent provider the record to loan should have.
     *
     * @return the ID of the format at the getContent provider the record to loan should have
     */
    public String getContentProviderFormatId() {
        return contentProviderFormatId;
    }
}
