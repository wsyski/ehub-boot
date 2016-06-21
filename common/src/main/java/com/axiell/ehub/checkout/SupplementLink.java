package com.axiell.ehub.checkout;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SupplementLink implements Serializable {
    private final SupplementLinkDTO supplementLinkDTO;

    public SupplementLink(final String name, final String href) {
        supplementLinkDTO = new SupplementLinkDTO().name(name).href(href);
    }

    public SupplementLink(SupplementLinkDTO supplementLinkDTO) {
        this.supplementLinkDTO = supplementLinkDTO;
    }

    public String name() {
        return supplementLinkDTO.getName();
    }

    public String href() {
        return supplementLinkDTO.getHref();
    }

    public SupplementLinkDTO toDTO() {
        return supplementLinkDTO;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
