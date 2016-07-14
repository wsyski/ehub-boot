package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ZinioExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<String> {
    protected static final String MESSAGE = "message";

    private String message;

    @Before
    public void setUpUnderTest() {
        underTest = new ZinioExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }


    @Test
    public void messageUnknown() {
        givenMessage(MESSAGE);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
        thenInternalServerErrorExceptionWithStatusUnknown();
    }

    @Test
    public void messageUnexistedMagazineRbid() {
        givenMessage(ZinioExceptionFactory.MESSAGE_UNEXISTED_MAGAZINE_RBID);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(ZinioExceptionFactory.MESSAGE_UNEXISTED_MAGAZINE_RBID);
        internalServerErrorExceptionWithStatusProductUnavailable();
    }

    private void givenMessage(final String message) {
        this.message = message;
    }

    @Override
    protected void whenCreateExecuted() {
        internalServerErrorException = underTest.create(message);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ZINIO;
    }
}
