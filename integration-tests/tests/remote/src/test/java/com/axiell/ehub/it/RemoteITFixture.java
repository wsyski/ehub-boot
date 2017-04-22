package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionMatcher;
import com.axiell.ehub.error.LmsErrorExceptionMatcher;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.ITestDataResource;
import com.axiell.ehub.test.TestData;
import com.axiell.ehub.test.TestDataConstants;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.core.HttpHeaders;
import java.util.Locale;

public abstract class RemoteITFixture extends PalmaITFixture {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteITFixture.class);
    private static final String LF = System.getProperty("line.separator");
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    private static final int PORT_NO = 16518;
    private static final String EHUB_SERVER_URI = "axiell-server-uri";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(16521);

    protected TestData testData;
    protected AuthInfo authInfo;
    protected IEhubService underTest;

    @Before
    public void setUp() throws EhubException {
        setEhubServer();
        setUpEhubClient();
        initTestData();
        initAuthInfo();
    }

    @After
    public void tearDown() {
        deleteTestData();
        WireMock.reset();
    }

    private void initTestData() {
        ITestDataResource testDataResource = getTestDataResource();
        testData = testDataResource.init(getContentProviderName(), isLoanPerProduct());
        LOGGER.info("Test data initialized: " + testData.toString());
    }

    private void deleteTestData() {
        ITestDataResource testDataResource = getTestDataResource();
        javax.ws.rs.core.Response response = testDataResource.delete();
        if (response.getStatus() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
            LOGGER.info("Test data deleted");
        } else {
            throw new IllegalStateException("Could not delete test data: " + response.getStatusInfo().getReasonPhrase());
        }
        LOGGER.info("Test data deleted");
    }

    private String getTestDataServiceBaseUri() {
        return System.getProperty(EHUB_SERVER_URI);
    }

    private void setEhubServer() {
        System.setProperty(EHUB_SERVER_URI, "http://localhost:" + PORT_NO);
        System.setProperty("catalina.base", "target");
        wireMockRule.addMockServiceRequestListener(this::logRequests);
        wireMockRule.addMockServiceRequestListener(this::validateRequests);
    }

    private void logRequests(final Request request, final Response response) {
        RequestMethod requestMethod = request.getMethod();
        String requestBodyAsString =
                requestMethod.equals(RequestMethod.POST) || requestMethod.equals(RequestMethod.PUT) ? LF + request.getBodyAsString() : StringUtils.EMPTY;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String authorizationHeaderAsString = authorizationHeader == null ? StringUtils.EMPTY : LF + HttpHeaders.AUTHORIZATION + ": " + authorizationHeader;
        LOGGER.info("wireMock: " + request.getMethod() + " " + request.getAbsoluteUrl() + authorizationHeaderAsString + requestBodyAsString + LF + response.getBodyAsString() +
                LF + response.getStatus());
    }

    private void validateRequests(final Request request, final Response response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (getContentProviderName().equals( TestDataConstants.CONTENT_PROVIDER_TEST_EP) && request.getUrl().startsWith("/ep/api/v1/")) {
            Assert.assertNotNull(HttpHeaders.AUTHORIZATION +" header can not be null",authorizationHeader);
        }
    }

    private void setUpEhubClient() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext("/com/axiell/ehub/secret-key-context.xml","/com/axiell/ehub/remote-client-context.xml");
        underTest = IEhubService.class.cast(springContext.getBean("ehubClient"));
    }

    private void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId()).libraryCard(testData.getLibraryCard())
                .pin(testData.getPin()).patronId(testData.getPatronId()).email(testData.getEmail()).build();
    }

    private ITestDataResource getTestDataResource() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(getTestDataServiceBaseUri());
        return target.proxy(ITestDataResource.class);
    }

    protected void givenExpectedContentProviderErrorException(final String status) {
        expectedException.expect(new ContentProviderErrorExceptionMatcher(EhubException.class, getContentProviderName(), status));
    }

    protected void givenExpectedLmsErrorException(final String status) {
        expectedException.expect(new LmsErrorExceptionMatcher(EhubException.class, testData.getEhubConsumerId(), status));
    }

    protected void givenExpectedEhubException(final Class clazz, final ErrorCause errorCause, final ErrorCauseArgument... arguments) {
        expectedException.expect(new EhubExceptionMatcher(EhubException.class, errorCause, arguments));
    }

    protected String getContentProviderAlias() {
        return TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + getContentProviderName();
    }

    protected abstract boolean isLoanPerProduct();

    protected abstract String getContentProviderName();
}
