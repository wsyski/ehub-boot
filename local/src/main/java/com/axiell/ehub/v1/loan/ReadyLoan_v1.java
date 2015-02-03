/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import javax.xml.bind.annotation.*;

/**
 * Represents a ready loan. It contains an {@link com.axiell.ehub.loan.LmsLoan} and a {@link ContentProviderLoan_v1}.
 */
@XmlRootElement(name = "readyLoan")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({LmsLoan_v1.class, ContentProviderLoan_v1.class})
public class ReadyLoan_v1 {
    private Long id;
    private LmsLoan_v1 lmsLoan;
    private ContentProviderLoan_v1 contentProviderLoan;

    /**
     * Empty constructor required by JAXB.
     */
    protected ReadyLoan_v1() {
    }

    public ReadyLoan_v1(Long id, LmsLoan_v1 lmsLoan, ContentProviderLoan_v1 contentProviderLoan) {
        this.id = id;
        this.lmsLoan = lmsLoan;
        this.contentProviderLoan = contentProviderLoan;
    }

    @XmlAttribute(name = "id", required = true)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @XmlElement(name = "lmsLoan", required = true)
    public LmsLoan_v1 getLmsLoan() {
        return lmsLoan;
    }

    protected void setLmsLoan(LmsLoan_v1 lmsLoan) {
        this.lmsLoan = lmsLoan;
    }

    @XmlElement(name = "contentProviderLoan", required = true)
    public ContentProviderLoan_v1 getContentProviderLoan() {
        return contentProviderLoan;
    }

    /**
     * 
     * @param contentProviderLoan
     */
    protected void setContentProviderLoan(ContentProviderLoan_v1 contentProviderLoan) {
        this.contentProviderLoan = contentProviderLoan;
    }
}
