package com.axiell.ehub.error;

import com.axiell.ehub.*;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class EhubExceptionMatcher extends TypeSafeMatcher<Throwable> {

    private final Class<? extends IEhubException> clazz;
    private final ErrorCause errorCause;
    private final ErrorCauseArgument[] arguments;

    public EhubExceptionMatcher(final Class<? extends IEhubException> clazz, final ErrorCause errorCause, final ErrorCauseArgument... arguments) {
        this.clazz = clazz;
        this.errorCause = errorCause;
        this.arguments = arguments;
    }

    @Override
    public boolean matchesSafely(final Throwable throwable) {
        if (!throwable.getClass().isAssignableFrom(clazz)) {
            return false;
        }
        EhubError ehubError = clazz.cast(throwable).getEhubError();
        ErrorCause errorCause = ehubError.getCause();
        if (!errorCause.equals(this.errorCause)) {
            return false;
        }
        List<ErrorCauseArgument> arguments = ehubError.getArguments();
        if (this.arguments.length != arguments.size()) {
            return false;
        }
        for (int i = 0; i < this.arguments.length; i++) {
            ErrorCauseArgument expectedArgument = this.arguments[i];
            ErrorCauseArgument actualArgument = arguments.get(i);
            if (!expectedArgument.equals(actualArgument)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected class ").appendValue(clazz).appendText(" error cause: ")
                .appendValue(errorCause).appendText(" arguments count: ").appendValue(arguments.length);
    }
}
