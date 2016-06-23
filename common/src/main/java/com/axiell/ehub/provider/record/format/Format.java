/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Set;

public class Format implements Serializable {
    private final FormatDTO dto;

    public Format(final FormatDTO formatDTO) {
        dto = formatDTO;
    }

    public Format(final String formatId, final String name, final String description, final ContentDisposition contentDisposition, final Set<String> platforms) {
        Validate.notNull(platforms);
        dto = new FormatDTO(formatId,name,contentDisposition).description(description).platforms(platforms);
    }

    public String id() {
        return dto.getId();
    }

    public String name() {
        return dto.getName();
    }

    public boolean locked() {
        return dto.isLocked();
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

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Format)) {
            return false;
        }
        final Format rhs = (Format) obj;
        return new EqualsBuilder().append(toDTO(), rhs.toDTO()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(toDTO()).toHashCode();
    }

    @Override
    public final String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
