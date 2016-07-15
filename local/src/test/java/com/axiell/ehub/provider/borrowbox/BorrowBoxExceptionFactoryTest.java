package com.axiell.ehub.provider.borrowbox;

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
public class BorrowBoxExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<ErrorDTO> {
    private static final String MESSAGE = "message";
    private static final String STATUS_NOT_AVAILABLE = "notAvailable";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new BorrowBoxExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessageAndStatusUnknown() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, AbstractContentProviderExceptionFactory.UNKNOWN_STATUS_CODE);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
        thenExpectedContentProviderErrorException(AbstractContentProviderExceptionFactory.UNKNOWN_STATUS_CODE);
    }

    @Test
    public void errorEntityWithMessageAndStatusProductNotFound() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, STATUS_NOT_AVAILABLE);
        whenCreateExecuted();
        thenExpectedContentProviderErrorException(ErrorCauseArgumentType.PRODUCT_UNAVAILABLE.name());
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
        return ContentProvider.CONTENT_PROVIDER_BORROWBOX;
    }
}
