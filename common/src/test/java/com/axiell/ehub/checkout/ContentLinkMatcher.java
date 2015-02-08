package com.axiell.ehub.checkout;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class ContentLinkMatcher extends BaseMatcher<ContentLink> {
    private final ContentLink contentLink;

    private ContentLinkMatcher(ContentLink contentLink) {
        this.contentLink = contentLink;
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof ContentLink) {
            ContentLink actualContentLink = (ContentLink) item;
            return contentLink.href().equals(actualContentLink.href());
        } else
            return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a ContentLink");
    }

    @Factory
    public static ContentLinkMatcher matchesExpectedContentLink(ContentLink contentLink) {
        return new ContentLinkMatcher(contentLink);
    }
}