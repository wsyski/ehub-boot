/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.ep;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoTest extends ContentProviderDataAccessorTestFixture {
    private static final long TIMESTAMP = 1436350109L;
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String SITE_ID = "siteId";
    private static final String SECRET_KEY = "secretKey";

    @Test
    public void testToString() throws EhubException {
        givenContentProviderConsumerProperties();
        givenPatronIdInPatron();
        AuthInfo authInfo = new AuthInfo(contentProviderConsumer, patron);
        authInfo.setTimestamp(TIMESTAMP);
        assertThat(authInfo.toString(), Matchers.is(
                "realm=\"provider TEST_EP\" site_id=\"siteId\" ehub_consumer_id=\"1\", ehub_patron_id=\"patronId\" timestamp=\"1436350109\", signature=\"Oa6LMvQbdsFJ8BxB%2BY02g1tXcKY%3D\""));
    }

    private void givenContentProviderConsumerProperties() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(SECRET_KEY);

    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
