package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryTestFixture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OcdExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new OcdExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessage() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, null);
        whenCreateExecuted();
        thenInternalServerErrorWithOnlyMessage(MESSAGE);
    }

    @Test
    public void errorEntityWithMessageAndStatusLibraryLimitReached() {
        givenErrorEntityWithMessageAndStatus(OcdExceptionFactory.MESSAGE_NO_FULFILLMENT_COPY_AVAILABLE, null);
        whenCreateExecuted();
        internalServerErrorExceptionWithLibraryLimitReached();
    }

    private void givenErrorEntityWithMessageAndStatus(final String message, final String status) {
        given(error.getMessage()).willReturn(message);
        given(response.readEntity(ErrorDTO.class)).willReturn(error);
    }

    @Override
    protected void whenCreateExecuted() {
        internalServerErrorException = underTest.create(response);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_OCD;
    }
}
