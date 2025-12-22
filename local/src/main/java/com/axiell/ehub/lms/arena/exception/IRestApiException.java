package com.axiell.ehub.lms.arena.exception;

import com.axiell.ehub.lms.arena.error.ArenaLocalRestApiError;

public interface IRestApiException {
    ArenaLocalRestApiError getRestApiError();

}
