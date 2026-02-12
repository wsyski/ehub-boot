package com.axiell.ehub.mock.util;

import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.InternalServerErrorException;

import java.util.List;

public class EhubJsonException {

    private ErrorCause cause;
    private String message;

    private List<ErrorCauseArgument> arguments;

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

    public List<ErrorCauseArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<ErrorCauseArgument> arguments) {
        this.arguments = arguments;
    }

    public InternalServerErrorException toException() {
        return new InternalServerErrorException(message, cause, arguments.toArray(new ErrorCauseArgument[0]));
    }
}
