/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.Validate;

import com.axiell.ehub.provider.ContentProvider;

/**
 * Represents a format of a record at a specific {@link ContentProvider}.
 */
@XmlAccessorType(XmlAccessType.NONE)
public final class Format implements Serializable {
    private static final long serialVersionUID = 5161980869973204797L;
    private String id;
    private String name;
    private String description;
    private String iconUrl;
    
    /**
     * Empty constructor required by JAXB. 
     */
    protected Format() {        
    }
    
    /**
     * Constructs a new {@link Format}.
     * 
     * @param id the ID of the format
     * @param name the translated name of the format
     * @param description the translated description of the format, can be <code>null</code>
     * @param iconUrl the URL to the icon of the format, can be <code>null</code> 
     */
    public Format(String id, String name, String description, String iconUrl) {
        Validate.notNull(id, "ID can't be null");
        Validate.notNull(name, "Name can't be null");
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }
    
    /**
     * Returns the ID of the format.
     *
     * @return the ID of the format
     */
    @XmlAttribute(name = "id", required = true)
    public String getId() {
        return id;
    }
    
    /**
     * Sets the ID of the format.
     *
     * @param id the ID of the format to set
     */
    protected void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the translated name of the formate.
     *
     * @return the translated name of the format
     */
    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name;
    }
    
    /**
     * Sets the translated name of the format.
     *
     * @param name the translated name of the format to set
     */
    protected void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the translated description of the format.
     *
     * @return the translated description of the format, can be <code>null</code>
     */
    @XmlAttribute(name = "description")
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the translated description of the format.
     *
     * @param description the translated description of the format to set
     */
    protected void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns the URL to the icon of the format.
     *
     * @return the URL to the icon of the format, can be <code>null</code>
     */
    @XmlAttribute(name = "iconUrl")
    public String getIconUrl() {
        return iconUrl;
    }
    
    /**
     * Sets the URL to the icon of the format.
     *
     * @param iconUrl the URL to the icon of the format to set
     */
    protected void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
