package com.axiell.ehub.provider.askews;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AskewsExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<String> {
    private static final String MESSAGE = "message";

    @Before
    public void setUpUnderTest() {
        underTest = new AskewsExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessage() {
        givenErrorEntityWithMessageAndStatus(MESSAGE);
        whenCreateExecuted();
        thenInternalServerErrorWithOnlyMessage(MESSAGE);
    }

    @Test
    public void errorEntityWithMessageAndStatusLibraryLimitReached() {
        givenErrorEntityWithMessageAndStatus(AskewsExceptionFactory.MESSAGE_NO_LICENCES_AVAILABLE);
        whenCreateExecuted();
        thenExpectedContentProviderErrorException(ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED.name());
    }

    private void givenErrorEntityWithMessageAndStatus(final String message) {
        given(response.readEntity(String.class)).willReturn(message);
    }

    @Override
    protected void whenCreateExecuted() {
        internalServerErrorException = underTest.create(response);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ASKEWS;
    }
}
