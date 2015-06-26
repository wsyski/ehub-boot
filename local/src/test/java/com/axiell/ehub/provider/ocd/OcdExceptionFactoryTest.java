package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.provider.ContentProviderExceptionFactoryFixture;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OcdExceptionFactoryTest extends ContentProviderExceptionFactoryFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";
    protected static final String MESSAGE_LIBRARY_LIMIT_REACHED = "No fulfillment copy available.";

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
        givenErrorEntityWithMessageAndStatus(MESSAGE_LIBRARY_LIMIT_REACHED, null);
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
    protected ContentProviderName getContentProviderName() {
        return ContentProviderName.OCD;
    }
}
