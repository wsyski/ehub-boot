package com.axiell.ehub.it26.util;

import com.axiell.ehub.checkout.ContentLink;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class ContentLinkMatcher extends BaseMatcher<ContentLink> {
    private final ContentLink expectedContentLink;

    public ContentLinkMatcher(ContentLink expectedContentLink) {
        this.expectedContentLink = expectedContentLink;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof ContentLink))
            return false;
        ContentLink actualContentLink = (ContentLink) item;
        return expectedContentLink.href().equals(actualContentLink.href());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a ContentLink");
    }

    @Factory
    public static ContentLinkMatcher matchesExpectedContentLink(ContentLink expectedContentLink) {
        return new ContentLinkMatcher(expectedContentLink);
    }
}
