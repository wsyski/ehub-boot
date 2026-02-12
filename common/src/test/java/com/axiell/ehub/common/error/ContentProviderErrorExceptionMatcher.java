package com.axiell.ehub.common.error;

import com.axiell.ehub.common.EhubError;
import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.IEhubException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class ContentProviderErrorExceptionMatcher extends TypeSafeMatcher<Throwable> {

    private final Class<? extends IEhubException> clazz;
    private final String status;
    private final String contentProviderName;

    public ContentProviderErrorExceptionMatcher(final Class<? extends IEhubException> clazz, final String contentProviderName, final String status) {
        this.clazz = clazz;
        this.contentProviderName = contentProviderName;
        this.status = status;
    }

    @Override
    public boolean matchesSafely(final Throwable throwable) {
        if (!throwable.getClass().isAssignableFrom(clazz)) {
            return false;
        }
        EhubError ehubError = clazz.cast(throwable).getEhubError();
        ErrorCause errorCause = ehubError.getCause();
        if (!errorCause.equals(ErrorCause.CONTENT_PROVIDER_ERROR)) {
            return false;
        }
        List<ErrorCauseArgument> arguments = ehubError.getArguments();
        if (arguments.size() < 2) {
            return false;
        }
        ErrorCauseArgument errorCauseArgument = arguments.get(0);
        if (!errorCauseArgument.getType().equals(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME)) {
            return false;
        }
        if (!errorCauseArgument.getValue().equals(contentProviderName)) {
            return false;
        }
        errorCauseArgument = arguments.get(1);
        if (!errorCauseArgument.getType().equals(ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS)) {
            return false;
        }
        if (!status.equals(errorCauseArgument.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected class ").appendValue(clazz).appendText(" provider: ")
                .appendValue(contentProviderName).appendText(" status: ").appendValue(status);
    }
}
