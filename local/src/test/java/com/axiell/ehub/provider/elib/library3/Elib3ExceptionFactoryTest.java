package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderExceptionFactoryTestFixture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Elib3ExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";

    @Mock
    private ErrorDTO error;

    @Before
    public void setUpUnderTest() {
        underTest = new Elib3ExceptionFactory(contentProviderConsumer, LANGUAGE, ehubExceptionFactory);
    }

    @Test
    public void errorEntityWithMessage() {
        givenErrorEntityWithReason();
        whenCreateExecuted();
        thenInternalServerErrorExceptionHasMessage(MESSAGE);
    }

    private void givenErrorEntityWithReason() {
        given(error.getReason()).willReturn(MESSAGE);
        given(response.readEntity(ErrorDTO.class)).willReturn(error);
    }

    @Override
    protected void whenCreateExecuted() {
        internalServerErrorException = underTest.create(response);
    }

    @Override
    protected String getContentProviderName()  {
       return ContentProvider.CONTENT_PROVIDER_ELIB3;
    }
}
