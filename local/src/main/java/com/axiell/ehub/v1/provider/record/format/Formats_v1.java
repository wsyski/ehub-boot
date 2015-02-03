/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.provider.record.format;

import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents record formats at a specific {@link com.axiell.ehub.provider.ContentProvider}.
 */
@XmlRootElement(name = "contentProviderFormats")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(Format_v1.class)
public class Formats_v1 {
    private Set<Format_v1> formats = new HashSet<>();

    /**
     * Returns a {@link java.util.Set} of {@link Format_v1}s.
     *
     * @return a {@link java.util.Set} of {@link Format_v1}s
     */
    @XmlElement(name = "contentProviderFormat", required = true)
    public Set<Format_v1> getFormats() {
        return formats;
    }

    /**
     * Sets a {@link java.util.Set} of {@link Format_v1}s.
     *
     * @param formats the {@link java.util.Set} of {@link Format_v1}s to set
     * @throws NullPointerException if the provided {@link java.util.Set} is null
     */
    protected void setFormats(Set<Format_v1> formats) {
        Validate.notNull(formats, "Can't add a Set of ContentProviderFormat objects that is null");
        this.formats = formats;
    }

    /**
     * Adds a {@link Format_v1} to the underlying {@link java.util.Set} of {@link Format_v1}s.
     *
     * @param format the {@link Format_v1} to add
     * @throws NullPointerException if the provided {@link Format_v1} is null
     */
    public void addFormat(Format_v1 format) {
        Validate.notNull(format, "Can't add a ContentProviderFormat that is null");
        formats.add(format);
    }

    /**
     * Returns a {@link java.util.List} of {@link Format_v1}s.
     *
     * @return a {@link java.util.List} of {@link Format_v1}s.
     */
    public List<Format_v1> asList() {
        return new ArrayList<>(formats);
    }
}
