package com.axiell.ehub.provider.elib.library3;

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
public class LegacyElib3ErrorResponseBodyReaderTest {
    private static final String REASON = "reason1";
    private LegacyElib3ErrorResponseBodyReader underTest;
    @Mock
    private ClientResponse<?> response;
    @Mock
    private ErrorResponse errorResponse;
    private String actualMessage;

    @Before
    public void setUpUnderTest() {
        underTest = new LegacyElib3ErrorResponseBodyReader();
    }

    @Test
    public void noErrorResponse() {
        whenRead();
        thenActualMessageIsNull();
    }

    @Test
    public void errorResponse() {
        givenErrorResponseWithReason();
        whenRead();
        thenActualMessageIsReason();
    }

    private void givenErrorResponseWithReason() {
        given(errorResponse.getReason()).willReturn(REASON);
        given(response.getEntity(ErrorResponse.class)).willReturn(errorResponse);
    }

    private void thenActualMessageIsReason() {
        assertThat(actualMessage, is(REASON));
    }

    private void thenActualMessageIsNull() {
        assertNull(actualMessage);
    }

    private void whenRead() {
        actualMessage = underTest.read(response);
    }
}
