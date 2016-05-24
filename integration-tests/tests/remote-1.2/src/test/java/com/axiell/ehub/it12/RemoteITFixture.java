/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.it12;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.TestData;
import com.axiell.ehub.test.TestDataConstants;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.core.HttpHeaders;

public abstract class RemoteITFixture {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteITFixture.class);
    private static final String LF = System.getProperty("line.separator");
    private static final int PORT_NO = 16518;
    private static final String EHUB_SERVER_URI = "axiell-server-uri";
    protected static final String CONTENT_PROVIDER_ALIAS = "Distribut\u00f6r: " + TestDataConstants.CONTENT_PROVIDER_TEST_EP;
    protected TestData testData;
    protected AuthInfo authInfo;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(16521);

    @Before
    public void setUp() throws Exception {
        setEhubServer();
        setUpEhubClient();
        initTestData();
        initAuthInfo();
    }

    private void initTestData() throws Exception {
        final ClientRequest request = new ClientRequest(getTestDataServiceBaseUri());
        final ClientResponse<TestData> response = request.post(TestData.class);
        testData = response.getEntity();
        LOGGER.info("Test data initialized: " + testData.toString());
    }

    private String getTestDataServiceBaseUri() {
        return System.getProperty(EHUB_SERVER_URI) + "/v3/test-data";
    }

    private void setEhubServer() {
        System.setProperty(EHUB_SERVER_URI, "http://localhost:" + PORT_NO);
        System.setProperty("catalina.base", "target");
        wireMockRule.addMockServiceRequestListener(this::logRequests);
    }

    private void logRequests(final Request request, final Response response) {
        RequestMethod requestMethod = request.getMethod();
        String requestBodyAsString =
                requestMethod.equals(RequestMethod.POST) || requestMethod.equals(RequestMethod.PUT) ? LF + request.getBodyAsString() : StringUtils.EMPTY;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String authorizationHeaderAsString = authorizationHeader == null ? StringUtils.EMPTY : LF + HttpHeaders.AUTHORIZATION + ": " + authorizationHeader;
        LOGGER.info(request.getMethod() + " " + request.getAbsoluteUrl() + authorizationHeaderAsString + requestBodyAsString + LF + response.getBodyAsString() +
                LF + response.getStatus());
    }

    private void setUpEhubClient() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/remote-client-context.xml"});
        Object bean = springContext.getBean("ehubClient");
        castBeanToIEhubService(bean);
    }

    protected abstract void castBeanToIEhubService(Object bean);

    protected abstract void initAuthInfo() throws EhubException;

    @After
    public void tearDown() throws Exception {
        deleteTestData();
        WireMock.reset();
    }

    private void deleteTestData() throws Exception {
        final ClientRequest request = new ClientRequest(getTestDataServiceBaseUri());
        final ClientResponse<?> response = request.delete();
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            LOGGER.info("Test data deleted");
        } else {
            final String reason = response.getEntity(String.class);
            throw new IllegalStateException("Could not delete test data: " + reason);
        }
    }
}
