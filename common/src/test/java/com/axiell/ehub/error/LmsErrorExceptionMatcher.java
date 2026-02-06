package com.axiell.ehub.error;

import com.axiell.ehub.*;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class LmsErrorExceptionMatcher extends TypeSafeMatcher<Throwable> {

    private final Class<? extends IEhubException> clazz;
    private final String status;
    private final long ehubConsumerId;

    public LmsErrorExceptionMatcher(final Class<? extends IEhubException> clazz, final long ehubConsumerId, final String status) {
        this.clazz = clazz;
        this.ehubConsumerId = ehubConsumerId;
        this.status = status;
    }

    @Override
    public boolean matchesSafely(final Throwable throwable) {
        if (!throwable.getClass().isAssignableFrom(clazz)) {
            return false;
        }
        EhubError ehubError = clazz.cast(throwable).getEhubError();
        ErrorCause errorCause = ehubError.getCause();
        if (!errorCause.equals(ErrorCause.LMS_ERROR)) {
            return false;
        }
        List<ErrorCauseArgument> arguments = ehubError.getArguments();
        if (arguments.size() < 2) {
            return false;
        }
        ErrorCauseArgument errorCauseArgument = arguments.get(0);
        if (!errorCauseArgument.getType().equals(ErrorCauseArgument.Type.LMS_STATUS)) {
            return false;
        }
        if (!errorCauseArgument.getValue().equals(status)) {
            return false;
        }
        errorCauseArgument = arguments.get(1);
        if (!errorCauseArgument.getType().equals(ErrorCauseArgument.Type.EHUB_CONSUMER_ID)) {
            return false;
        }
        if (!errorCauseArgument.getValue().equals(String.valueOf(ehubConsumerId))) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected class ").appendValue(clazz).appendText(" ehub consumer id: ")
                .appendValue(ehubConsumerId).appendText(" status: ").appendValue(status);
    }
}
