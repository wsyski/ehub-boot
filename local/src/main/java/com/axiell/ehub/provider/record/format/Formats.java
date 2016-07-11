package com.axiell.ehub.provider.record.format;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Formats {
    private Set<Format> formats = new HashSet<>();

    public Set<Format> getFormats() {
        return formats;
    }

    public void addFormat(final Format format) {
        Validate.notNull(format, "Format can not be null");
        formats.add(format);
    }

    public List<Format> asList() {
        return new ArrayList<>(formats);
    }
}
