/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlValue;

import com.axiell.ehub.provider.ContentProvider;

/**
 * Represents an argument to an {@link ErrorCause}. It can for example be the name of a {@link ContentProvider} or the
 * ID of a loan in the LMS.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorCauseArgument {
    private Type type;
    private String value;

    /**
     * Default constructor required by JAXB.
     */
    protected ErrorCauseArgument() {
    }

    /**
     * Constructs a new {@link ErrorCauseArgument}.
     * 
     * @param type the {@link Type} of the {@link ErrorCauseArgument}
     * @param value the value of the {@link ErrorCauseArgument}
     */
    public ErrorCauseArgument(final Type type, final Object value) {
        this.type = type;
        this.value = value == null ? null : value.toString();
    }

    /**
     * Returns the type.
     * 
     * @return the type
     */
    @XmlAttribute(name = "type", required = true)
    public Type getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    protected void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the value.
     * 
     * @return the value
     */
    @XmlValue
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param value the value to set
     */
    protected void setValue(String value) {
        this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Represents the type of the {@link ErrorCauseArgument}.
     */
    @XmlEnum
    public static enum Type {
        READY_LOAN_ID,
        LMS_LOAN_ID,
        LMS_RECORD_ID,
        LMS_STATUS,
        CONTENT_PROVIDER_NAME,
        EHUB_CONSUMER_ID,
        CONTENT_PROVIDER_STATUS,
        CONTENT_PROVIDER_LOAN_ID,
        FORMAT_ID,
        CONTENT_PROVIDER_RECORD_ID,
        LIBRARY_CARD,
        EHUB_CONSUMER_PROPERTY_KEY,
        EHUB_CONSUMER_PROPERTY_VALUE,
        FIELD
    }
}
