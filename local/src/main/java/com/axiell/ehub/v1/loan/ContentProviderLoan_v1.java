/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Represents a loan at a Content Provider.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ContentProviderLoanMetadata_v1.class, DownloadableContent_v1.class, StreamingContent_v1.class})
public class ContentProviderLoan_v1 {
    private final ContentProviderLoanMetadata_v1 metadata;
    private IContent_v1 content;

    /**
     * Default constructor required by JAXB.
     */
    protected ContentProviderLoan_v1() {
        this.metadata = new ContentProviderLoanMetadata_v1();
    }

    /**
     * Constructs a new {@link ContentProviderLoan_v1}.
     *
     * @param metadata the metadata of the {@link ContentProviderLoan_v1}
     * @param content the contentLink of the {@link ContentProviderLoan_v1}
     */
    public ContentProviderLoan_v1(ContentProviderLoanMetadata_v1 metadata, IContent_v1 content) {
        Validate.notNull(metadata, "The ContentProviderLoanMetadata can't be null");
        this.metadata = metadata;
        this.content = content;
    }

    /**
     * @see com.axiell.ehub.loan.ContentProviderLoanMetadata#getId()
     */
    @XmlAttribute(name = "id", required = true)
    public String getId() {
        return metadata.getId();
    }

    /**
     * @see com.axiell.ehub.loan.ContentProviderLoanMetadata#setId(String)
     */
    protected void setId(String id) {
        metadata.setId(id);
    }

    /**
     * @see com.axiell.ehub.loan.ContentProviderLoanMetadata#getExpirationDate()
     */
    @XmlAttribute(name = "expirationDate", required = true)
    public Date getExpirationDate() {
        return metadata.getExpirationDate();
    }

    /**
     * @see com.axiell.ehub.loan.ContentProviderLoanMetadata#setExpirationDate(java.util.Date)
     */
    protected void setExpirationDate(final Date expirationDate) {
        metadata.setExpirationDate(expirationDate);
    }

    /**
     * Returns the {@link com.axiell.ehub.loan.IContent}.
     *
     * @return an instance of either {@link com.axiell.ehub.loan.DownloadableContent} or {@link com.axiell.ehub.loan.StreamingContent}
     */
    @XmlElements(value = {@XmlElement(name = "downloadableContent", type = DownloadableContent_v1.class, required = true),
        @XmlElement(name = "streamingContent", type = StreamingContent_v1.class, required = true),

    })
    public IContent_v1 getContent() {
        return content;
    }

    /**
     * Sets the {@link com.axiell.ehub.loan.IContent}. Only used by JAXB.
     *
     * @param content an instance of either {@link com.axiell.ehub.loan.DownloadableContent} or {@link com.axiell.ehub.loan.StreamingContent} to set
     */
    protected void setContent(IContent_v1 content) {
        this.content = content;
    }

    /**
     * Returns the underlying {@link com.axiell.ehub.loan.ContentProviderLoanMetadata}.
     *
     * @return a {@link com.axiell.ehub.loan.ContentProviderLoanMetadata}
     */
    @XmlTransient
    public ContentProviderLoanMetadata_v1 getMetadata() {
        return metadata;
    }

}
