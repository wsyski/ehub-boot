/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.language;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Defines a supported language in the eHUB.
 */
@Entity
@Table(name = "CONTENT_P_FORMAT_LANGUAGE")
@Access(AccessType.PROPERTY)
public class Language {
    private String id;

    /**
     * Default constructor required by JPA.
     */
    protected Language() {                
    }    

    /**
     * Returns the ID of the {@link Language}, i.e. the ISO 639 alpha-2 or alpha-3 language code.
     *
     * @return an  ISO 639 alpha-2 or alpha-3 language code
     */
    @Id
    @Column(name = "LANGUAGE")
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
}
