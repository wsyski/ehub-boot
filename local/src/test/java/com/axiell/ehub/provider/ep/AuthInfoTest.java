package com.axiell.ehub.provider.ep;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.ContentProvider;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AuthInfoTest {
    private static final long TIMESTAMP = 1436350109L;
    private static final long EHUB_CONSUMER_ID = 1L;
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final String SITE_ID = "siteId";
    private static final String SECRET_KEY = "secretKey";
    private static final String PATRON_ID = "patronId";
    private static final String LIBRARY_CARD = "card";

    private AuthInfo underTest;

    @Mock
    private ContentProviderConsumer contentProviderConsumer;

    @Mock
    private ContentProvider contentProvider;

    @Mock
    private EhubConsumer ehubConsumer;

    @Mock
    protected Patron patron;

    @Test
    public void toStringWithPatronId() throws EhubException {
        givenContentProviderConsumerProperties(EpUserIdValue.PATRON_ID);
        givenPatronIdInPatron();
        whenAuthInfoCreated();
        thenExpectedToStringWithPatronId();
    }

    @Test
    public void toStringWithLibraryCard() throws EhubException {
        givenContentProviderConsumerProperties(EpUserIdValue.LIBRARY_CARD);
        givenLibraryCardInPatron();
        whenAuthInfoCreated();
        thenExpectedToStringWithLibraryCard();
    }

    private void thenExpectedToStringWithPatronId() {
        assertThat(underTest.toString(), Matchers.is(
                "realm=\"provider TEST_EP\", site_id=\"siteId\", ehub_consumer_id=\"1\", user_id=\"patronId\", timestamp=\"1436350109\", signature=\"Oa6LMvQbdsFJ8BxB%2BY02g1tXcKY%3D\""));
    }

    private void thenExpectedToStringWithLibraryCard() {
        assertThat(underTest.toString(), Matchers.is(
                "realm=\"provider TEST_EP\", site_id=\"siteId\", ehub_consumer_id=\"1\", user_id=\"card\", timestamp=\"1436350109\", signature=\"GhP%2BCqzIkS4b2TF7ak6Gb5qZGeo%3D\""));
    }

    private void whenAuthInfoCreated() {
        underTest = new AuthInfo(contentProviderConsumer, patron);
        ReflectionTestUtils.setField(underTest, "timestamp", TIMESTAMP);
    }

    private void givenContentProviderConsumerProperties(final EpUserIdValue epUserIdValue) {
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SITE_ID)).willReturn(SITE_ID);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_SECRET_KEY)).willReturn(SECRET_KEY);
        given(contentProviderConsumer.getProperty(ContentProviderConsumer.ContentProviderConsumerPropertyKey.EP_USER_ID_VALUE))
                .willReturn(epUserIdValue.name());
        given(contentProviderConsumer.getEhubConsumer()).willReturn(ehubConsumer);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    private void givenPatronIdInPatron() {
        given(patron.hasId()).willReturn(true);
        given(patron.getId()).willReturn(PATRON_ID);
    }

    private void givenLibraryCardInPatron() {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(LIBRARY_CARD);
    }
}
