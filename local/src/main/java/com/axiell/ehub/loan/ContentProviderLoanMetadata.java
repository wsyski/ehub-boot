/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.util.DateFactory;
import com.axiell.ehub.util.Validate;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents the metadata of a loan at a Content Provider.
 */
@Embeddable
@Access(AccessType.PROPERTY)
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({FormatDecoration.class})
public class ContentProviderLoanMetadata implements Serializable {
    private String id;
    private ContentProvider contentProvider;
    private Date expirationDate;
    private FormatDecoration formatDecoration;
    private String recordId;

    /**
     * Empty constructor required by JPA.
     */
    protected ContentProviderLoanMetadata() {
    }

    private ContentProviderLoanMetadata(String id, ContentProvider contentProvider, Date expirationDate, String recordId, FormatDecoration formatDecoration) {
        this.id = id;
        this.contentProvider = contentProvider;
        this.expirationDate = DateFactory.create(expirationDate);
        this.recordId = recordId;
        this.formatDecoration = formatDecoration;
    }

    /**
     * Returns the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider}.
     *
     * @return the ID of the loan at a {@link com.axiell.ehub.provider.ContentProvider}
     */
    @Column(name = "CONTENT_PROVIDER_LOAN_ID", nullable = true)
    @XmlAttribute(name = "id", required = true)
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
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false)
    @ForeignKey(name = "FK_EHUB_LOAN_CONTENT_PROVIDER")
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
    @XmlAttribute(name = "expirationDate", required = true)
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
     * {@link com.axiell.ehub.provider.ContentProvider}.
     *
     * @return a {@link com.axiell.ehub.provider.record.format.FormatDecoration}
     */
    @JoinColumn(name = "CONTENT_P_FORMAT_DECORATION_ID", nullable = false)
    @ForeignKey(name = "FK_CONTENT_P_L_M_CONTENT_P_F_D")
    @ManyToOne
    @XmlElement(name = "formatDecoration", required = true)
    public FormatDecoration getFormatDecoration() {
        return formatDecoration;
    }

    /**
     * Sets the decoration of the format of the loan at the
     * {@link com.axiell.ehub.provider.ContentProvider}. Only used by JPA.
     *
     * @param formatDecoration the decoration of the format of the loan at the
     *                         {@link com.axiell.ehub.provider.ContentProvider} to set
     */
    protected void setFormatDecoration(FormatDecoration formatDecoration) {
        this.formatDecoration = formatDecoration;
    }

    /**
     * NOTE: nullable = true should be changed to nullable = false when all
     * loans in the Ehub database has a record ID.
     */
    @Column(name = "CONTENT_PROVIDER_RECORD_ID", nullable = true)
    public String getRecordId() {
        return recordId;
    }

    protected void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public static class Builder {
        private final ContentProvider contentProvider;
        private final Date expirationDate;
        private final FormatDecoration formatDecoration;
        private final String recordId;

        private String id;

        public Builder(final ContentProvider contentProvider, final Date expirationDate, final String recordId, final FormatDecoration formatDecoration) {
            Validate.isNotNull(contentProvider, "ContentProvider can't be null");
            Validate.isNotNull(expirationDate, "Expiration date can't be null");
            Validate.isNotNull(recordId, "The record ID can't be null");
            Validate.isNotNull(formatDecoration, "Format decoration can't be null");
            this.contentProvider = contentProvider;
            this.expirationDate = DateFactory.create(expirationDate);
            this.recordId = recordId;
            this.formatDecoration = formatDecoration;
        }

        public Builder contentProviderLoanId(final String value) {
            this.id = value;
            return this;
        }

        public ContentProviderLoanMetadata build() {
            return new ContentProviderLoanMetadata(id, contentProvider, expirationDate, recordId, formatDecoration);
        }
    }
}
