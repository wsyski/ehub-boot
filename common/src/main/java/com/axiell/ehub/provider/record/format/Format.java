/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.controller.external.v5_0.provider.dto.FormatDTO;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Set;

public class Format implements Serializable {
    private final FormatDTO formatDTO;

    public Format(final FormatDTO formatDTO) {
        this.formatDTO = formatDTO;
    }

    public Format(final String formatId, final String name, final String description, final ContentDisposition contentDisposition, final Set<String> platforms,
                  final boolean isLocked) {
        Validate.notNull(platforms);
        formatDTO = new FormatDTO(formatId,name,contentDisposition).description(description).platforms(platforms).locked(isLocked);
    }

    public String getId() {
        return formatDTO.getId();
    }

    public String getName() {
        return formatDTO.getName();
    }

    public boolean isLocked() {
        return formatDTO.isLocked();
    }

    public String getDescription() {
        return formatDTO.getDescription();
    }

    public ContentDisposition getContentDisposition() {
        return formatDTO.getContentDisposition();
    }

    public Set<String> getPlatforms() {
        return formatDTO.getPlatforms();
    }

    public FormatDTO toDTO() {
        return formatDTO;
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
