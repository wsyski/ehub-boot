package com.axiell.ehub.provider.overdrive;

import org.jboss.resteasy.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class LegacyOverdriveErrorResponseBodyReaderTest {
    private static final String MESSAGE = "MSG1";
    private LegacyOverdriveErrorResponseBodyReaderLegacy underTest;
    @Mock
    private ClientResponse<?> response;
    @Mock
    private ErrorDetails errorDetails;
    private String actualMessage;

    @Before
    public void setUpUnderTest() {
        underTest = new LegacyOverdriveErrorResponseBodyReaderLegacy();
    }

    @Test
    public void noErrorDetails() {
        whenRead();
        thenActualMessageIsNull();
    }

    @Test
    public void errorDetails() {
        givenErrorDetailsWithMessage();
        whenRead();
        thenActualMessageIsReason();
    }

    private void givenErrorDetailsWithMessage() {
        given(errorDetails.getMessage()).willReturn(MESSAGE);
        given(response.getEntity(ErrorDetails.class)).willReturn(errorDetails);
    }

    private void thenActualMessageIsReason() {
        assertThat(actualMessage, is(MESSAGE));
    }

    private void thenActualMessageIsNull() {
        assertNull(actualMessage);
    }

    private void whenRead() {
        actualMessage = underTest.read(response);
    }
}
