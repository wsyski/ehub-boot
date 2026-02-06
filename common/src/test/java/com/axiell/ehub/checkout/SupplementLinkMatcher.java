package com.axiell.ehub.checkout;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class SupplementLinkMatcher extends BaseMatcher<SupplementLink> {
    private final SupplementLink expectedSupplementLink;

    public SupplementLinkMatcher(SupplementLink expectedContentLink) {
        this.expectedSupplementLink = expectedContentLink;
    }

    public static SupplementLinkMatcher matchesExpectedSupplementLink(final SupplementLink expectedSupplementLink) {
        return new SupplementLinkMatcher(expectedSupplementLink);
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof SupplementLink))
            return false;
        SupplementLink actualSupplementLink = (SupplementLink) item;
        return expectedSupplementLink.href().equals(actualSupplementLink.href()) && expectedSupplementLink.name().equals(actualSupplementLink.name());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matching a SupplementLink");
    }
}
