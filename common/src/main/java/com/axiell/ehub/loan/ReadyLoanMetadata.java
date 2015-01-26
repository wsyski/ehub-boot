/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.xml.bind.annotation.*;

/**
 * Represents the metadata of a ready loan, i.e. everything except the actual content.
 */
@XmlRootElement(name = "readyLoanMetadata")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({LmsLoan.class, ContentProviderLoanMetadata.class})
public class ReadyLoanMetadata {
    private Long id;
    private LmsLoan lmsLoan;
    private ContentProviderLoanMetadata contentProviderLoanMetadata;

    /**
     * Empty constructor required by JAXB.
     */
    protected ReadyLoanMetadata() {
    }

    public ReadyLoanMetadata(Long id, LmsLoan lmsLoan, ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.id = id;
        this.lmsLoan = lmsLoan;
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
    }

    @XmlAttribute(name = "id", required = true)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    @XmlElement(name = "lmsLoan", required = true)
    public LmsLoan getLmsLoan() {
        return lmsLoan;
    }

    protected void setLmsLoan(LmsLoan lmsLoan) {
        this.lmsLoan = lmsLoan;
    }
    
    @XmlElement(name = "contentProviderLoanMetadata", required = true)
    public ContentProviderLoanMetadata getContentProviderLoanMetadata() {
        return contentProviderLoanMetadata;
    }


    protected void setContentProviderLoanMetadata(ContentProviderLoanMetadata contentProviderLoanMetadata) {
        this.contentProviderLoanMetadata = contentProviderLoanMetadata;
    }
}
