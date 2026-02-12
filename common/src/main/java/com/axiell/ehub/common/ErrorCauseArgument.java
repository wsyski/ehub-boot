package com.axiell.ehub.common;

import com.axiell.ehub.common.provider.ContentProvider;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Represents an argument to an {@link ErrorCause}. It can for example be the name of a {@link ContentProvider} or the
 * ID of a loan in the LMS.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorCauseArgument implements Serializable {
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
     * @param type  the {@link Type} of the {@link ErrorCauseArgument}
     * @param value the value of the {@link ErrorCauseArgument}
     */
    public ErrorCauseArgument(final Type type, final Object value) {
        this.type = type;
        this.value = value == null ? null : value.toString();
    }

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
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ErrorCauseArgument)) {
            return false;
        }
        final ErrorCauseArgument rhs = (ErrorCauseArgument) obj;
        return new EqualsBuilder().append(getType(), rhs.getType()).append(getValue(), rhs.getValue()).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 31).append(getType()).append(getValue()).toHashCode();
    }

    /**
     * Represents the type of the {@link ErrorCauseArgument}.
     */
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
