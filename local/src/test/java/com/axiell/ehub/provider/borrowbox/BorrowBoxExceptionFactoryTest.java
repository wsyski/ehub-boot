package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.provider.ContentProviderExceptionFactoryFixture;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BorrowBoxExceptionFactoryTest extends ContentProviderExceptionFactoryFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";
    private static final String STATUS_NOT_FOUND = "notAvailable";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new BorrowBoxExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessageAndStatusUnknown() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, STATUS_UNKNOWN);
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
        thenInternalServerErrorExceptionWithStatusUnknown();
    }

    @Test
    public void errorEntityWithMessageAndStatusProductNotFound() {
        givenErrorEntityWithMessageAndStatus(MESSAGE, STATUS_NOT_FOUND);
        whenCreateExecuted();
        internalServerErrorExceptionWithStatusProductUnavailable();
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
    protected ContentProviderName getContentProviderName() {
        return ContentProviderName.BORROWBOX;
    }
}
