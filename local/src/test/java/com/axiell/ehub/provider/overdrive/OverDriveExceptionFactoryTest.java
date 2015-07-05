package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryFixture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OverDriveExceptionFactoryTest extends ContentProviderExceptionFactoryFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new OverDriveExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessageAndStatusUnknown() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, STATUS_UNKNOWN);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
        thenInternalServerErrorExceptionWithStatusUnknown();
    }

    @Test
    public void errorEntityWithMessageAndStatusLibraryLimitReached() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, OverDriveExceptionFactory.STATUS_NO_COPIES_AVAILABLE);
        whenCreateExecuted();
        internalServerErrorExceptionWithLibraryLimitReached();
    }

    private void givenErrorEntityWithMessageAndStatus(final String message, final String status) {
        given(error.getMessage()).willReturn(message);
        given(error.getErrorCode()).willReturn(status);
        given(response.readEntity(ErrorDTO.class)).willReturn(error);
    }

    @Override
    protected void whenCreateExecuted() {
        internalServerErrorException = underTest.create(response);
    }

    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_OVERDRIVE;
    }
}
