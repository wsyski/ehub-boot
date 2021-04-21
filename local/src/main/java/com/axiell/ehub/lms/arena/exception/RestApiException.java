package com.axiell.ehub.lms.arena.exception;
import com.axiell.ehub.lms.arena.error.RestApiError;

public final class RestApiException extends Exception implements IRestApiException {
    private final RestApiError restApiError;

    public RestApiException(RestApiError restApiError) {
        super(restApiError.getMessage());
        this.restApiError = restApiError;
    }

    public RestApiError getRestApiError() {
        return restApiError;
    }
}
