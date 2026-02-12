/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EhubError implements Serializable {
    private ErrorCause cause;
    private String message;
    private List<ErrorCauseArgument> arguments = new ArrayList<>();

    public EhubError(final ErrorCause cause, final String message) {
        Validate.notNull(cause, "The cause can't be null");
        Validate.notNull(message, "The message can't be null");
        this.cause = cause;
        this.message = message;
    }

    public EhubError(final ErrorCause cause, final String message, final ErrorCauseArgument... args) {
        Validate.notNull(cause, "The cause can't be null");
        Validate.notNull(message, "The message can't be null");
        Validate.notNull(args, "The arguments can't be null");
        this.cause = cause;
        this.message = message;
        Collections.addAll(arguments, args);
    }
}
