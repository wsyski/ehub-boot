/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.ehub.util.HashCodeBuilderFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Represents a loan in the Library Management System (LMS).
 */
@Embeddable
@Access(AccessType.PROPERTY)
@XmlAccessorType(XmlAccessType.NONE)
public class LmsLoan_v1 implements Serializable {
    private String id;

    /**
     * Empty constructor required by JAXB.
     */
    protected LmsLoan_v1() {
    }
    
    /**
     * Constructs a new {@link LmsLoan_v1}.
     *
     * @param id the ID of the loan in the LMS
     */
    public LmsLoan_v1(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the loan in the LMS.
     *
     * @return the ID of the loan in the LMS
     */
    @Column(name = "LMS_LOAN_ID", nullable = false)
    @XmlAttribute(name = "id", required = true)
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
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LmsLoan_v1)) {
            return false;
        }
        LmsLoan_v1 rhs = (LmsLoan_v1) obj;
        return new EqualsBuilder().append(id, rhs.getId()).isEquals();
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilderFactory.create().append(id).toHashCode();
    }
}
