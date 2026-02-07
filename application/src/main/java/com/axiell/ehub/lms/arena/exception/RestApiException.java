package com.axiell.ehub.lms.arena.exception;
import com.axiell.ehub.lms.arena.error.ArenaLocalRestApiError;

public final class RestApiException extends Exception implements IRestApiException {
    private final ArenaLocalRestApiError restApiError;

    public RestApiException(ArenaLocalRestApiError restApiError) {
        super(restApiError.getMessage());
        this.restApiError = restApiError;
    }

    public ArenaLocalRestApiError getRestApiError() {
        return restApiError;
    }
}
