/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Represents a ready loan. It contains an {@link LmsLoan} and a {@link ContentProviderLoan}.
 */
@XmlRootElement(name = "readyLoan")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({LmsLoan.class, ContentProviderLoan.class})
public class ReadyLoan {
    private Long id;
    private LmsLoan lmsLoan;
    private ContentProviderLoan contentProviderLoan;

    /**
     * Empty constructor required by JAXB.
     */
    protected ReadyLoan() {
    }
    
    /**
     * Constructs a new {@link ReadyLoan}.
     * 
     * @param id the ID globally unique ID of the {@link ReadyLoan}
     * @param lmsLoan the {@link LmsLoan}
     * @param contentProviderLoan the {@link ContentProviderLoan}
     */
    public ReadyLoan(Long id, LmsLoan lmsLoan, ContentProviderLoan contentProviderLoan) {
        this.id = id;
        this.lmsLoan = lmsLoan;
        this.contentProviderLoan = contentProviderLoan;
    }

    /**
     * Returns the ID globally unique ID of the {@link ReadyLoan}.
     *
     * @return the ID globally unique ID of the {@link ReadyLoan}
     */
    @XmlAttribute(name = "id", required = true)
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID globally unique ID of the {@link ReadyLoan}.
     *
     * @param id the ID globally unique ID of the {@link ReadyLoan} to set
     */
    protected void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Returns the lmsLoan.
     *
     * @return the lmsLoan
     */
    @XmlElement(name = "lmsLoan", required = true)
    public LmsLoan getLmsLoan() {
        return lmsLoan;
    }

    /**
     * Sets the lmsLoan.
     *
     * @param lmsLoan the lmsLoan to set
     */
    protected void setLmsLoan(LmsLoan lmsLoan) {
        this.lmsLoan = lmsLoan;
    }
    
    /**
     * Returns the contentProviderLoan.
     *
     * @return the contentProviderLoan
     */
    @XmlElement(name = "contentProviderLoan", required = true)
    public ContentProviderLoan getContentProviderLoan() {
        return contentProviderLoan;
    }

    /**
     * 
     * @param contentProviderLoan
     */
    protected void setContentProviderLoan(ContentProviderLoan contentProviderLoan) {
        this.contentProviderLoan = contentProviderLoan;
    }
}
