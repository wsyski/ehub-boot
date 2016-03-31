/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.Set;

public class Format implements Serializable {
    private final FormatDTO dto;

    public Format(FormatDTO formatDTO) {
        dto = formatDTO;
    }

    public Format(final String formatId, final String name, final String description, final ContentDisposition contentDisposition, final Set<String> platforms) {
        Validate.notNull(platforms);
        dto = new FormatDTO().id(formatId).name(name).description(description).contentDisposition(contentDisposition).platforms(platforms);
    }

    public String id() {
        return dto.getId();
    }

    public String name() {
        return dto.getName();
    }

    public String description() {
        return dto.getDescription();
    }

    public ContentDisposition contentDisposition() {
        return dto.getContentDisposition();
    }

    public Set<String> platforms() {
        return dto.getPlatforms();
    }

    public FormatDTO toDTO() {
        return dto;
    }
}
