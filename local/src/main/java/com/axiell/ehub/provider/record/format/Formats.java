/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;

public class Formats {
    private Set<Format> formats = new HashSet<>();

    public Set<Format> getFormats() {
        return formats;
    }

    public void addFormat(Format format) {
        Validate.notNull(format, "Can't add a ContentProviderFormat that is null");
        formats.add(format);
    }
}
