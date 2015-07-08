/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoTest extends ContentProviderDataAccessorTestFixture {
    private static final long TIMESTAMP = 1434616780L;
    private static final String LIBRARY_CARD = "ehub1";
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String BORROWBOX_LIBRARY_ID = "developmentPartner";
    private static final String BORROWBOX_SITE_ID = "4390";
    private static final String BORROWBOX_SECRET_KEY = "nbYJzxJ2dg/WO0KVYz6T9n4n1hRCpgui8DCIm5uFRbE=";
    private AuthInfo underTest;

    @Test
    public void testToString() throws EhubException {
        givenContentProviderConsumerProperties();
        givenLibraryCardInPatron(LIBRARY_CARD);
        whenAuthInfoCreated();
        thenExpectedToString();
    }

    private void givenContentProviderConsumerProperties() {
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_LIBRARY_ID))
                .willReturn(BORROWBOX_LIBRARY_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SITE_ID)).willReturn(BORROWBOX_SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.BORROWBOX_SECRET_KEY))
                .willReturn(BORROWBOX_SECRET_KEY);
    }

    private void thenExpectedToString() {
        assertThat(underTest.toString(), Matchers.is(
                "Credential=developmentPartner, SignatureDate=1434616780, Signature=Y1ayI8QqC8MFr4TbMYGqsJswIWx9SM0jMSU/uj5aLbA="));
    }

    private void whenAuthInfoCreated() {
        underTest = new AuthInfo(contentProviderConsumer, patron);
        ReflectionTestUtils.setField(underTest, "timestamp", TIMESTAMP);
    }

    @Override
    protected String getContentProviderName() {
        return CONTENT_PROVIDER_TEST_EP;
    }
}
