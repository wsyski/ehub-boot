package com.axiell.ehub.provider.zinio;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ZinioPatronLoginIT extends AbstractZinioIT {
    private String loginUrl;

    @Test
    public void login() {
        givenConfigurationProperties();
        givenPatron();
        givenContentProvider();
        whenLogin();
        thenExpectedLoginUrl();
    }

    private void whenLogin() {
        loginUrl = underTest.login(contentProviderConsumer, patron);
    }

    private void thenExpectedLoginUrl() {
        Assert.assertThat(loginUrl, Matchers.startsWith("http://www.rbdigitaltest.com/axielltest?p_session="));
    }


}