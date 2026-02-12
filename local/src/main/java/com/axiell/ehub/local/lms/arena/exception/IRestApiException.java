package com.axiell.ehub.local.lms.arena.exception;

import com.axiell.ehub.local.lms.arena.error.ArenaLocalRestApiError;

public interface IRestApiException {
    ArenaLocalRestApiError getRestApiError();

}
