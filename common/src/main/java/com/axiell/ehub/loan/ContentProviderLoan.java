/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.Validate;

/**
 * Represents a loan at a Content Provider.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ContentProviderLoanMetadata.class, DownloadableContent.class, StreamingContent.class})
public class ContentProviderLoan {
    private final ContentProviderLoanMetadata metadata;
    private IContent content;

    /**
     * Default constructor required by JAXB.
     */
    protected ContentProviderLoan() {
        this.metadata = new ContentProviderLoanMetadata();
    }

    /**
     * Constructs a new {@link ContentProviderLoan}.
     * 
     * @param metadata the metadata of the {@link ContentProviderLoan}
     * @param content the content of the {@link ContentProviderLoan}
     */
    public ContentProviderLoan(ContentProviderLoanMetadata metadata, IContent content) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.content = content;
    }

    /**
     * @see ContentProviderLoanMetadata#getId()
     */
    @XmlAttribute(name = "id", required = true)
    public String getId() {
        return metadata.getId();
    }

    /**
     * @see ContentProviderLoanMetadata#setId(String)
     */
    protected void setId(String id) {
        metadata.setId(id);
    }

    /**
     * @see ContentProviderLoanMetadata#getExpirationDate()
     */
    @XmlAttribute(name = "expirationDate", required = true)
    public Date getExpirationDate() {
        return metadata.getExpirationDate();
    }

    /**
     * @see ContentProviderLoanMetadata#setExpirationDate(Date)
     */
    protected void setExpirationDate(final Date expirationDate) {
        metadata.setExpirationDate(expirationDate);
    }

    /**
     * Returns the {@link IContent}.
     * 
     * @return an instance of either {@link DownloadableContent} or {@link StreamingContent}
     */
    @XmlElements(value = {@XmlElement(name = "downloadableContent", type = DownloadableContent.class, required = true),
        @XmlElement(name = "streamingContent", type = StreamingContent.class, required = true),

    })
    public IContent getContent() {
        return content;
    }

    /**
     * Sets the {@link IContent}. Only used by JAXB.
     * 
     * @param content an instance of either {@link DownloadableContent} or {@link StreamingContent} to set
     */
    protected void setContent(IContent content) {
        this.content = content;
    }

    /**
     * Returns the underlying {@link ContentProviderLoanMetadata}.
     * 
     * @return a {@link ContentProviderLoanMetadata}
     */
    @XmlTransient
    public ContentProviderLoanMetadata getMetadata() {
        return metadata;
    }

}
