package com.axiell.ehub;

import static org.mockito.BDDMockito.given;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.aspectj.lang.JoinPoint;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.util.XjcSupport;

@RunWith(MockitoJUnitRunner.class)
public class EhubClientExceptionAspectTest {
    private static final String INVALID_ENTITY = "xml";
    private static final ErrorCause BAD_REQUEST = ErrorCause.BAD_REQUEST;
    private EhubClientExceptionAspect underTest;
    @Mock
    private JoinPoint joinPoint;
    @Mock
    private ClientResponseFailure clientResponseFailure;
    @Mock
    private ClientResponse<?> clientResponse;
    @Mock
    private MultivaluedMap<String, String> headers;
    @Mock
    private List<String> contentTypes;

    @Before
    public void setUp() {
	underTest = new EhubClientExceptionAspect();
    }

    @Test
    public void noContentTypes() {
	givenResponse();
	givenHeaders();
	try {
	    underTest.toEhubException(joinPoint, clientResponseFailure);
	} catch (EhubException e) {
	    thenErrorCauseIsInternalServerError(e);
	}
    }

    private void givenResponse() {
	given(clientResponseFailure.getResponse()).willReturn(clientResponse);
    }

    private void givenHeaders() {
	given(clientResponse.getHeaders()).willReturn(headers);
    }

    private void thenErrorCauseIsInternalServerError(EhubException e) {
	ErrorCause actualErrorCause = getErrorCause(e);
	Assert.assertEquals(ErrorCause.INTERNAL_SERVER_ERROR, actualErrorCause);
    }

    private ErrorCause getErrorCause(EhubException e) {
	EhubError ehubError = e.getEhubError();
	return ehubError.getCause();
    }

    @Test
    public void noEntity() {
	givenResponse();
	givenHeaders();
	givenContentTypes();
	givenXmlContentType();
	try {
	    underTest.toEhubException(joinPoint, clientResponseFailure);
	} catch (EhubException e) {
	    thenErrorCauseIsInternalServerError(e);
	}
    }

    private void givenContentTypes() {
	given(headers.get(HttpHeaders.CONTENT_TYPE)).willReturn(contentTypes);
    }

    private void givenXmlContentType() {
	given(contentTypes.contains(MediaType.APPLICATION_XML)).willReturn(true);
    }

    @Test
    public void noXmlContentType() {
	givenResponse();
	givenHeaders();
	givenContentTypes();
	try {
	    underTest.toEhubException(joinPoint, clientResponseFailure);
	} catch (EhubException e) {
	    thenErrorCauseIsInternalServerError(e);
	}
    }
    
    @Test
    public void jaxbException() throws JAXBException {
	givenResponse();
	givenHeaders();
	givenContentTypes();
	givenXmlContentType();
	givenInvalidEntity();
	try {
	    underTest.toEhubException(joinPoint, clientResponseFailure);
	} catch (EhubException e) {
	    thenErrorCauseIsInternalServerError(e);
	}
    }

    private void givenInvalidEntity() {
	given(clientResponse.getEntity(String.class)).willReturn(INVALID_ENTITY);
    }

    @Test
    public void toEhubException() throws JAXBException {
	givenResponse();
	givenHeaders();
	givenContentTypes();
	givenXmlContentType();
	givenBadRequestInXmlFormatAsEntity();
	try {
	    underTest.toEhubException(joinPoint, clientResponseFailure);
	} catch (EhubException e) {
	    thenErrorCauseIsBadRequest(e);
	}
    }

    private void givenBadRequestInXmlFormatAsEntity() {
	final EhubError ehubError = BAD_REQUEST.toEhubError();
	final String xml = XjcSupport.marshal(ehubError);
	given(clientResponse.getEntity(String.class)).willReturn(xml);
    }

    private void thenErrorCauseIsBadRequest(EhubException e) {
	ErrorCause actualErrorCause = getErrorCause(e);
	Assert.assertEquals(BAD_REQUEST, actualErrorCause);
    }
}
