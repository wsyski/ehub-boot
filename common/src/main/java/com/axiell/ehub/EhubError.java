/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang3.Validate;

/**
 * Represents an error in the eHUB. It contains the cause of the error, a formatted error message and potentially some
 * arguments to the cause that can be used if the client wants to create its own error message.
 * 
 * <p>
 * It is included in the body of all responses from the eHUB with status code > 399.
 * </p>
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(ErrorCauseArgument.class)
public class EhubError implements Serializable {
    private ErrorCause cause;
    private String message;
    private List<ErrorCauseArgument> arguments = new ArrayList<>();

    /**
     * Default constructor required by JAXB.
     */
    protected EhubError() {
    }

    /**
     * Constructs a new {@link EhubError}.
     * 
     * @param cause the cause
     * @param message the formatted error message
     */
    EhubError(final ErrorCause cause, final String message) {
        Validate.notNull(cause, "The cause can't be null");
        Validate.notNull(message, "The message can't be null");
        this.cause = cause;
        this.message = message;
    }

    /**
     * Constructs a new {@link EhubError}.
     * 
     * @param cause the cause
     * @param message the formatted error message
     * @param args an array of {@link ErrorCauseArgument}s
     */
    EhubError(final ErrorCause cause, final String message, final ErrorCauseArgument... args) {
        Validate.notNull(cause, "The cause can't be null");
        Validate.notNull(message, "The message can't be null");
        Validate.notNull(args, "The arguments can't be null");
        this.cause = cause;
        this.message = message;
        Collections.addAll(arguments, args);
    }

    /**
     * Returns the cause.
     * 
     * @return the cause
     */
    @XmlAttribute(name = "cause", required = true)
    public ErrorCause getCause() {
        return cause;
    }

    /**
     * Sets the cause.
     * 
     * @param cause the cause to set
     */
    public void setCause(ErrorCause cause) {
        this.cause = cause;
    }

    /**
     * Returns the error message.
     * 
     * @return the error message
     */
    @XmlAttribute(name = "message", required = true)
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     * 
     * @param message the error message to set
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the arguments.
     * 
     * @return the arguments
     */
    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument", required = true)
    public List<ErrorCauseArgument> getArguments() {
        return arguments;
    }

    /**
     * Sets the arguments.
     * 
     * @param arguments the arguments to set
     */
    protected void setArguments(List<ErrorCauseArgument> arguments) {
        this.arguments = arguments;
    }
}
