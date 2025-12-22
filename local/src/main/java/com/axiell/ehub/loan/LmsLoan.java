/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import java.io.Serializable;

import  jakarta.persistence.Access;
import  jakarta.persistence.AccessType;
import  jakarta.persistence.Column;
import  jakarta.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.axiell.ehub.util.HashCodeBuilderFactory;

/**
 * Represents a loan in the Library Management System (LMS).
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class LmsLoan implements Serializable {
    private String id;

    /**
     * Empty constructor required by JAXB.
     */
    protected LmsLoan() {        
    }
    
    /**
     * Constructs a new {@link LmsLoan}.
     * 
     * @param id the ID of the loan in the LMS
     */
    public LmsLoan(String id) {
        this.id = id;
    }
    
    /**
     * Gets the ID of the loan in the LMS.
     * 
     * @return the ID of the loan in the LMS
     */
    @Column(name = "LMS_LOAN_ID", nullable = false)
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the loan in the LMS.
     * 
     * @param id the ID of the loan in the LMS to set
     */
    protected void setId(String id) {
        this.id = id;
    }   
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LmsLoan)) {
            return false;
        }
        LmsLoan rhs = (LmsLoan) obj;
        return new EqualsBuilder().append(id, rhs.getId()).isEquals();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilderFactory.create().append(id).toHashCode();
    }
}
