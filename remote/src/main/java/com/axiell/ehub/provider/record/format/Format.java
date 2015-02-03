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

public class Format {
    private final FormatDTO formatDTO;

    public Format(FormatDTO formatDTO) {
        this.formatDTO = formatDTO;
    }

    public String id() {
        return formatDTO.getId();
    }

    public String name() {
        return formatDTO.getName();
    }

    public String description() {
        return formatDTO.getDescription();
    }

    public ContentDisposition contentDisposition() {
        return formatDTO.getContentDisposition();
    }

}
