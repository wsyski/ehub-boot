/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.util.DateFactory;
import com.axiell.ehub.util.Validate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents the metadata of a loan at a Content Provider.
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class ContentProviderLoanMetadata implements Serializable {
    private String id;
    private ContentProvider contentProvider;
    private Date expirationDate;
    private FormatDecoration firstFormatDecoration;
    private String recordId;
    private String issueId;

    /**
     * Empty constructor required by JPA.
     */
    protected ContentProviderLoanMetadata() {
    }

    private ContentProviderLoanMetadata(final String id, final ContentProvider contentProvider, final Date expirationDate, final String recordId,
                                        final String issueId, final FormatDecoration firstFormatDecoration) {
        this.id = id;
        this.contentProvider = contentProvider;
        this.expirationDate = DateFactory.create(expirationDate);
        this.recordId = recordId;
        this.issueId = issueId;
        this.firstFormatDecoration = firstFormatDecoration;
    }

    /**
     * Returns the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider}.
     *
     * @return the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider}
     */
    @Column(name = "CONTENT_PROVIDER_LOAN_ID", nullable = true)
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider}. Only used by JPA
     * and JAXB.
     *
     * @param id the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider} to set
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the {@link com.axiell.ehub.provider.ContentProvider} where the loan is created.
     *
     * @return the {@link com.axiell.ehub.provider.ContentProvider} where the loan is created
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_EHUB_LOAN_CONTENT_PROVIDER"))
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Sets the {@link com.axiell.ehub.provider.ContentProvider} where the loan is created. Only used by
     * JPA and JAXB.
     *
     * @param contentProvider the {@link com.axiell.ehub.provider.ContentProvider} where the loan is created to set
     */
    protected void setContentProvider(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Returns the expiration date of the loan at the {@link com.axiell.ehub.provider.ContentProvider}.
     *
     * @return the expiration date of the loan at the {@link com.axiell.ehub.provider.ContentProvider}
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRATION_DATE", nullable = false)
    public Date getExpirationDate() {
        return DateFactory.create(expirationDate);
    }

    /**
     * Sets the expiration date of the loan at the {@link com.axiell.ehub.provider.ContentProvider}. Only
     * used by JPA and JAXB.
     *
     * @param expirationDate the expiration date of the loan at the {@link com.axiell.ehub.provider.ContentProvider}
     *                       to set
     */
    protected void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns the decoration of the format of the loan at the
     * {@link ContentProvider}.
     *
     * @return a {@link FormatDecoration}
     */
    @JoinColumn(name = "CONTENT_P_F_F_DECORATION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CONTENT_P_L_M_CONTENT_P_F_D"))
    @ManyToOne
    public FormatDecoration getFirstFormatDecoration() {
        return firstFormatDecoration;
    }

    /**
     * Sets the decoration of the format of the loan at the
     * {@link com.axiell.ehub.provider.ContentProvider}. Only used by JPA.
     *
     * @param firstFormatDecoration the decoration of the format of the loan at the
     *                              {@link com.axiell.ehub.provider.ContentProvider} to set
     */
    protected void setFirstFormatDecoration(final FormatDecoration firstFormatDecoration) {
        this.firstFormatDecoration = firstFormatDecoration;
    }


    @Column(name = "CONTENT_PROVIDER_RECORD_ID", nullable = false)
    public String getRecordId() {
        return recordId;
    }

    protected void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    @Column(name = "CONTENT_PROVIDER_ISSUE_ID")
    public String getContentProviderIssueId() {
        return issueId;
    }

    protected void setContentProviderIssueId(final String issueId) {
        this.issueId = issueId;
    }

    public static class Builder {
        private final ContentProvider contentProvider;
        private final Date expirationDate;
        private final FormatDecoration firstFormatDecoration;
        private final String recordId;
        private String id;
        private String issueId;

        public Builder(final ContentProvider contentProvider, final Date expirationDate, final String recordId, final FormatDecoration firstFormatDecoration) {
            Validate.isNotNull(contentProvider, "ContentProvider can't be null");
            Validate.isNotNull(expirationDate, "Expiration date can't be null");
            Validate.isNotNull(recordId, "The record ID can't be null");
            Validate.isNotNull(firstFormatDecoration, "First format decoration can't be null");
            this.contentProvider = contentProvider;
            this.expirationDate = DateFactory.create(expirationDate);
            this.recordId = recordId;
            this.firstFormatDecoration = firstFormatDecoration;
        }

        public Builder contentProviderLoanId(final String value) {
            this.id = value;
            return this;
        }

        public Builder issueId(final String issueId) {
            this.issueId = issueId;
            return this;
        }

        public ContentProviderLoanMetadata build() {
            return new ContentProviderLoanMetadata(id, contentProvider, expirationDate, recordId, issueId, firstFormatDecoration);
        }
    }
}
