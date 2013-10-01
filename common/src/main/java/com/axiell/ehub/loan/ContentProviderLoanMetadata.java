/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.util.Validate;

/**
 * Represents the metadata of a loan at a Content Provider.
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class ContentProviderLoanMetadata {
    private String id;
    private ContentProvider contentProvider;
    private Date expirationDate;
    private FormatDecoration formatDecoration;

    /**
     * Empty constructor required by JPA.
     */
    protected ContentProviderLoanMetadata() {
    }

    /**
     * Constructs a new {@link ContentProviderLoanMetadata}.
     * 
     * @param id the ID of the loan at the provided {@link ContentProvider}
     * @param contentProvider the {@link ContentProvider} where the loan is created
     * @param expirationDate the expiration date of the loan at the {@link ContentProvider}
     * @param formatDecoration a decoration of the format of the loan at the {@link ContentProvider}
     */
    public ContentProviderLoanMetadata(String id, ContentProvider contentProvider, Date expirationDate, FormatDecoration formatDecoration) {
        Validate.isNotNull(id, "ID can't be null");
        Validate.isNotNull(contentProvider, "ContentProvider can't be null");
        Validate.isNotNull(expirationDate, "Expiration date can't be null");
        Validate.isNotNull(formatDecoration, "Format decoration can't be null");
        this.id = id;
        this.contentProvider = contentProvider;
        this.expirationDate = initExpirationDate(expirationDate);
        this.formatDecoration = formatDecoration;
    }

    @Transient
    private Date initExpirationDate(Date expirationDate) {
	final long time = expirationDate.getTime(); 
	return new Date(time);
    }

    /**
     * Returns the ID of the loan at a {@link ContentProvider}.
     * 
     * @return the ID of the loan at a {@link ContentProvider}
     */
    @Column(name = "CONTENT_PROVIDER_LOAN_ID", nullable = false)
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the loan at a {@link ContentProvider}. Only used by JPA and JAXB.
     * 
     * @param id the ID of the loan at a {@link ContentProvider} to set
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the {@link ContentProvider} where the loan is created.
     * 
     * @return the {@link ContentProvider} where the loan is created
     */
    @ManyToOne
    @JoinColumn(name = "CONTENT_PROVIDER_ID", nullable = false)
    @ForeignKey(name = "FK_EHUB_LOAN_CONTENT_PROVIDER")
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Sets the {@link ContentProvider} where the loan is created. Only used by JPA and JAXB.
     * 
     * @param contentProvider the {@link ContentProvider} where the loan is created to set
     */
    protected void setContentProvider(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Returns the expiration date of the loan at the {@link ContentProvider}.
     * 
     * @return the expiration date of the loan at the {@link ContentProvider}
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRATION_DATE", nullable = false)
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the loan at the {@link ContentProvider}. Only used by JPA and JAXB.
     * 
     * @param expirationDate the expiration date of the loan at the {@link ContentProvider} to set
     */
    protected void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns the decoration of the format of the loan at the {@link ContentProvider}.
     * 
     * @return a {@link FormatDecoration}
     */
    @JoinColumn(name = "CONTENT_P_FORMAT_DECORATION_ID", nullable = false)
    @ForeignKey(name = "FK_CONTENT_P_L_M_CONTENT_P_F_D")
    @ManyToOne
    public FormatDecoration getFormatDecoration() {
        return formatDecoration;
    }

    /**
     * Sets the decoration of the format of the loan at the {@link ContentProvider}. Only used by JPA.
     * 
     * @param formatDecoration the decoration of the format of the loan at the {@link ContentProvider}
     * to set
     */
    protected void setFormatDecoration(FormatDecoration formatDecoration) {
        this.formatDecoration = formatDecoration;
    }
}
