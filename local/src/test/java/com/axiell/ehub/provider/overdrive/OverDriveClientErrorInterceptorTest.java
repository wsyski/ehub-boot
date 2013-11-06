package com.axiell.ehub.provider.overdrive;

import static com.axiell.ehub.EhubAssert.thenInternalServerErrorExceptionIsThrown;
import static org.mockito.BDDMockito.given;

import java.lang.reflect.Method;

import org.jboss.resteasy.client.ClientResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.EhubError;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.publit.IPublitTradeApi;

@RunWith(MockitoJUnitRunner.class)
public class OverDriveClientErrorInterceptorTest {
    private Class<ICirculationResource> CIRCULATION_RESOURCE_TYPE = ICirculationResource.class;
    private Class<IPublitTradeApi> PUBLIT_TRADE_API_TYPE = IPublitTradeApi.class;
    private static final int ERROR_STATUS = 500;

    private OverDriveClientErrorInterceptor underTest;
    private Method method;
    @Mock
    private ClientResponse<?> response;
    @Mock
    private ErrorDetails errorDetails;
    private boolean accept;

    @Before
    public void setUpOverDriveClientErrorInterceptor() {
	underTest = new OverDriveClientErrorInterceptor();
    }

    @Before
    public void setUpMethod() throws NoSuchMethodException {
	method = CIRCULATION_RESOURCE_TYPE.getMethod("getCheckouts", OAuthAccessToken.class);
    }

    @Test
    public void accept() {
	whenAcceptICirculationResource();
	thenClassIsAccepted();
    }

    private void whenAcceptICirculationResource() {
	accept = underTest.accept(CIRCULATION_RESOURCE_TYPE, method);
    }

    private void thenClassIsAccepted() {
	Assert.assertTrue(accept);
    }

    @Test
    public void notAccepted() {
	whenAcceptIPublitTradeApi();
	thenClassIsNotAccepted();
    }

    private void whenAcceptIPublitTradeApi() {
	accept = underTest.accept(PUBLIT_TRADE_API_TYPE, method);
    }

    private void thenClassIsNotAccepted() {
	Assert.assertFalse(accept);
    }

    @Test
    public void handle() {
	givenClientResponseStatus();
	givenErrorDetails();
	try {
	    whenHandle();
	    Assert.fail("An InternalServerErrorException should have been thrown");
	} catch (InternalServerErrorException e) {
	    thenInternalServerErrorExceptionIsThrown(e);
	    thenErrorCauseIsContentProviderError(e);
	}
    }

    private void whenHandle() {
	underTest.handle(response);
    }

    private void givenClientResponseStatus() {
	given(response.getStatus()).willReturn(ERROR_STATUS);
    }

    private void givenErrorDetails() {
	given(response.getEntity(ErrorDetails.class)).willReturn(errorDetails);
    }

    private void thenErrorCauseIsContentProviderError(InternalServerErrorException e) {
	EhubError ehubError = e.getEhubError();
	ErrorCause errorCause = ehubError.getCause();
	Assert.assertEquals(ErrorCause.CONTENT_PROVIDER_ERROR, errorCause);
    }
}
