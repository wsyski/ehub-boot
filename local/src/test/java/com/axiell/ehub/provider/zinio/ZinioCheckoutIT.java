package com.axiell.ehub.provider.zinio;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class ZinioCheckoutIT extends AbstractZinioIT {

    @Test
    public void checkout() {
        givenConfigurationProperties();
        givenPatron();
        givenContentProvider();
        whenCheckout();
        thenExpectedCheckout();
    }

    private void whenCheckout() {
        underTest.checkout(contentProviderConsumer, patron, ISSUE_ID, Locale.ENGLISH.getLanguage());
    }

    private void thenExpectedCheckout() {
    }
}