package com.axiell.ehub.provider.overdrive;

import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.provider.AbstractContentProviderExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryTestFixture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OverDriveExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new OverDriveExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessageAndStatusUnknown() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, AbstractContentProviderExceptionFactory.UNKNOWN_STATUS_CODE);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
        thenExpectedContentProviderErrorException(AbstractContentProviderExceptionFactory.UNKNOWN_STATUS_CODE);
    }

    @Test
    public void errorEntityWithMessageAndStatusLibraryLimitReached() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, OverDriveExceptionFactory.STATUS_NO_COPIES_AVAILABLE);
        whenCreateExecuted();
        thenExpectedContentProviderErrorException(ErrorCauseArgumentType.LIBRARY_LIMIT_REACHED.name());
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
