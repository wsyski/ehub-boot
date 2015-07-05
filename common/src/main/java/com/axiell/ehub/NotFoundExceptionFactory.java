package com.axiell.ehub;

import com.axiell.ehub.ErrorCauseArgument.Type;

public final class NotFoundExceptionFactory {

    private NotFoundExceptionFactory() {	
    }
    
    public static NotFoundException create(final String contentProviderName, final String recordId, final String formatId) {
	final ErrorCauseArgument recordIdArg = new ErrorCauseArgument(Type.CONTENT_PROVIDER_RECORD_ID, recordId);
        final ErrorCauseArgument formatIdArg = new ErrorCauseArgument(Type.FORMAT_ID, formatId);
        final ErrorCauseArgument contentProviderNameArg = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, contentProviderName);
        return new NotFoundException(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_FOUND, recordIdArg, formatIdArg, contentProviderNameArg);
    }
}
