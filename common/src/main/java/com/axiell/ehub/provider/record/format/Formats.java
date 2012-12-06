/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang3.Validate;

import com.axiell.ehub.provider.ContentProvider;

/**
 * Represents record formats at a specific {@link ContentProvider}. 
 */
@XmlRootElement(name = "contentProviderFormats")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(Format.class)
public final class Formats {
    private Set<Format> formats = new HashSet<>();
    
    /**
     * Returns a {@link Set} of {@link Format}s.
     *
     * @return a {@link Set} of {@link Format}s
     */
    @XmlElement(name = "contentProviderFormat", required = true)
    public Set<Format> getFormats() {
        return formats;
    }
    
    /**
     * Sets a {@link Set} of {@link Format}s.
     *
     * @param formats the {@link Set} of {@link Format}s to set
     * @throws NullPointerException if the provided {@link Set} is null
     */
    protected void setFormats(Set<Format> formats) {
        Validate.notNull(formats, "Can't add a Set of ContentProviderFormat objects that is null");
        this.formats = formats;
    }
    
    /**
     * Adds a {@link Format} to the underlying {@link Set} of {@link Format}s.
     * 
     * @param format the {@link Format} to add
     * @throws NullPointerException if the provided {@link Format} is null
     */
    public void addFormat(Format format) {
        Validate.notNull(format, "Can't add a ContentProviderFormat that is null");
        formats.add(format);
    }
    
    /**
     * Returns a {@link List} of {@link Format}s.
     * 
     * @return a {@link List} of {@link Format}s.
     */
    public List<Format> asList() {
        return new ArrayList<>(formats);
    }
}
