package com.axiell.ehub.error;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WebApplicationExceptionMatcher extends TypeSafeMatcher<Throwable> {

    private final Class<? extends WebApplicationException> clazz;
    private final String expectedBody;

    public WebApplicationExceptionMatcher(final Class<? extends WebApplicationException> clazz, final String expectedBody) {
        this.clazz = clazz;
        this.expectedBody = expectedBody;
    }

    @Override
    public boolean matchesSafely(final Throwable throwable) {
        if (!throwable.getClass().isAssignableFrom(clazz)) {
            return false;
        }
        Response response = clazz.cast(throwable).getResponse();
        String actualBody = response.readEntity(String.class);
        return (expectedBody == null && actualBody == null) || (actualBody != null && expectedBody != null && actualBody.contains(expectedBody));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected class ").appendValue(clazz).appendText(" containing: ").appendValue(expectedBody);
    }
}
