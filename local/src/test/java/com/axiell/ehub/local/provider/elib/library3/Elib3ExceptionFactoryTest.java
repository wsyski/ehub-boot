package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.provider.ContentProviderExceptionFactoryTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class Elib3ExceptionFactoryTest extends ContentProviderExceptionFactoryTestFixture<ErrorDTO> {
    protected static final String MESSAGE = "message";

    @Mock
    private ErrorDTO error;

    @BeforeEach
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
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_ELIB3;
    }
}
