/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.xml.bind.annotation.XmlEnum;
import java.text.MessageFormat;

/**
 * Represents the cause of an error within the eHUB.
 */
@XmlEnum
public enum ErrorCause {
    BAD_REQUEST("Some parameter(s) in your URL or XML are not correct. Please see the eHUB API documentation and review your request."),
    MISSING_CONTENT_PROVIDER_NAME("The getContent provider name is missing in your request. Please see the eHUB API documentation and review your request."),
    MISSING_AUTHORIZATION_HEADER("Authorization header is missing"),
    MISSING_EHUB_CONSUMER_ID("The eHUB Consumer ID is missing in the authentication information"), 
    MISSING_SIGNATURE("The signature is missing in the authentication information"), 
    MISSING_LIBRARY_CARD("The library card is missing in the authentication information"), 
    MISSING_PIN("The pin is missing in the authentication information"), 
    MISSING_EMAIL("Patron has no registered email address"),
    MISSING_FORMAT("The format is missing in the request"),
    MISSING_FIELD("The field with name {0} is missing in the request. Please see the API documentation for more information."),
    INVALID_SIGNATURE("Invalid signature"), 
    EHUB_CONSUMER_NOT_FOUND("An eHUB Consumer with ID {0} could not be found"),
    EHUB_CONSUMER_INVALID_PROPERTY("Invalid property {0} with the value {1} for the eHUB Consumer with ID {2}"),
    MISSING_SECRET_KEY("A secret key has not been provided for the eHUB Consumer with ID {0}"),
    UNKNOWN_CONTENT_PROVIDER("The getContent provider with name {0} is unknown to the eHUB"),
    LOAN_BY_LMS_LOAN_ID_NOT_FOUND("The loan with LMS loan ID {0} could not be found"), 
    LOAN_BY_ID_NOT_FOUND("The loan with ID {0} could not be found"), 
    CONTENT_PROVIDER_LOAN_NOT_FOUND("The loan with ID {0} at the Content Provider {1} could not be found"), 
    MISSING_CONTENT_PROVIDER_LOAN_ID("The ID of the loan at the Content Provider is missing"), 
    FORMAT_NOT_FOUND("The format with ID {0} could not be found for the Content Provider {1}"), 
    CONTENT_PROVIDER_RECORD_NOT_FOUND("The record with ID {0} in the format {1} could not be found at the Content Provider {2}"),
    CONTENT_PROVIDER_CONSUMER_NOT_FOUND("A Consumer for Content Provider with name {0} could not be found for EhubConsumer with ID {1}"), 
    INTERNAL_SERVER_ERROR("The eHUB server encountered an unexpected condition which prevented it from fulfilling the request. Please contact the Axiell Support."), 
    CONTENT_PROVIDER_ERROR("The Content Provider {0} returned the following error status code: {1}"),
    CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT("The Content Provider {0} does not support loan per product"),
    LMS_CHECKOUT_DENIED("Checkout of record with ID {0} to the holder of library card {1} is denied for the eHUB Consumer with ID {2}"),
    LMS_BLOCKED_LIBRARY_CARD("Blocked library card {0} for the eHUB Consumer with ID {1}"),
    LMS_LIBRARY_CARD_NOT_FOUND("The library card {0} not found for the eHUB Consumer with ID {1}"),
    LMS_INVALID_ACCOUNT_CARD("Invalid account for the library card {0} for the eHUB Consumer with ID {1}"),
    LMS_INVALID_LIBRARY_CARD("Invalid library card {0} for the eHUB Consumer with ID {1}"),
    LMS_INVALID_PIN_CODE("Invalid pin code for the library card {0} for the eHUB Consumer with ID {1}"),
    LMS_RECORD_NOT_FOUND("The record with ID {0} can not be found for the eHUB Consumer with ID {1}"),
    LMS_ERROR("LMS error with code {0} for the eHUB Consumer with ID {1}"),
    NOT_IMPLEMENTED("The eHUB server does not support the functionality required to fulfill the request. Please contact the Axiell Support.");

    private final String message;

    /**
     * Constructs a new {@link ErrorCause}.
     * 
     * @param message the error message
     */
    private ErrorCause(final String message) {
        this.message = message;
    }

    /**
     * Converts this {@link ErrorCause} to an {@link EhubError}.
     * 
     * @return an {@link EhubError}
     */
    public EhubError toEhubError() {
        return new EhubError(this, message);
    }

    /**
     * Converts this {@link ErrorCause} to an {@link EhubError}.
     * 
     * @param arguments an array of {@link ErrorCauseArgument} to be used for formatting the error message but also to
     * be included in the {@link EhubError}
     * @return an {@link EhubError}
     */
    public EhubError toEhubError(ErrorCauseArgument... arguments) {
        if (arguments == null) {
            return toEhubError();
        } else {
            final Object[] formatArgs = (Object[]) arguments;
            final String formattedMessage = MessageFormat.format(message, formatArgs);
            return new EhubError(this, formattedMessage, arguments);
        }
    }
}
