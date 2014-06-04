/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.language;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Defines a supported language in the eHUB.
 */
@Entity
@Table(name = "LANGUAGE")
@Access(AccessType.PROPERTY)
public class Language implements Serializable {
    private String id;

    /**
     * Default constructor required by JPA.
     */
    protected Language() {                
    }

    public Language(final String id) {
        this.id = id;
    }

    /**
     * Returns the ID of the {@link Language}, i.e. the ISO 639 alpha-2 or alpha-3 language code.
     *
     * @return an  ISO 639 alpha-2 or alpha-3 language code
     */
    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }
    
    /**
     * Sets the ID of the {@link Language}, i.e. the ISO 639 alpha-2 or alpha-3 language code.
     *
     * @param id the ISO 639 alpha-2 or alpha-3 language code to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {        
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Language rhs = (Language) obj;
        return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(getId()).toHashCode();
    }
}