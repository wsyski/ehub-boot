package com.axiell.ehub.lms.arena.exception;

import com.axiell.ehub.lms.arena.error.RestApiError;

public interface IRestApiException {
    RestApiError getRestApiError();

}
