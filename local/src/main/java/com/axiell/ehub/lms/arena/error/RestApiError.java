package com.axiell.ehub.lms.arena.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.Validate;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiError implements Serializable {
    public static final String ARGUMENT_THROWABLE_CLASS_NAME = "throwableClassName";
    private ErrorCause cause;
    private String message;
    private Map<String, String> arguments = new HashMap<>();
    private String stackTrace;

    private RestApiError() {
    }

    private RestApiError(final Builder builder) {
        this.cause = builder.cause;
        this.message = builder.message;
        this.arguments = builder.arguments;
        Throwable throwable = builder.throwable;
        if (throwable != null && builder.isDebug) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            stackTrace = sw.toString();
        }
    }

    public ErrorCause getCause() {
        return cause;
    }

    public void setCause(ErrorCause cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public static final class Builder implements Serializable {
        ErrorCause cause;
        String message;
        Throwable throwable;
        Map<String, String> arguments = new HashMap<>();
        boolean isDebug = false;

        public Builder(final ErrorCause cause, final String message) {
            Validate.notNull(cause, "The cause can not be null");
            Validate.notNull(message, "The message can not be null");
            this.cause = cause;
            this.message = message;
        }

        public Builder arguments(final Map<String, String> arguments) {
            this.arguments.putAll(arguments);
            return this;
        }

        public Builder argument(final String key, final String value) {
            arguments.put(key, value);
            return this;
        }

        public Builder throwable(final Throwable exception) {
            this.throwable = exception;
            return this;
        }

        public Builder isDebug(final boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }


        public RestApiError build() {
            return new RestApiError(this);
        }
    }
}
