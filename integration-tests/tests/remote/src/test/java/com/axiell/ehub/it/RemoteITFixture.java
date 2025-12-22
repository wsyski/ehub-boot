package com.axiell.ehub.it;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.EhubException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.config.ApplicationConfig;
import com.axiell.ehub.controller.provider.json.JsonProvider;
import com.axiell.ehub.error.ContentProviderErrorExceptionMatcher;
import com.axiell.ehub.error.EhubExceptionMatcher;
import com.axiell.ehub.error.LmsErrorExceptionMatcher;
import com.axiell.ehub.test.ITestRootResource;
import com.axiell.ehub.test.TestDataConstants;
import com.axiell.ehub.test.TestDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public abstract class RemoteITFixture extends WireMockITFixture {
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    private static final int EHUB_PORT_NO = 16518;
    private static final String EHUB_SERVER_URI = "EHUB_SERVER_URI";

    protected TestDataDTO testData;
    protected AuthInfo authInfo;
    protected IEhubService underTest;


    @BeforeEach
    public void setUp() throws EhubException {
        setEhubServer();
        setUpEhubClient();
        initTestData();
        initAuthInfo();
    }

    @AfterEach
    public void tearDown() {
        deleteTestData();
        resetWireMock();
    }

    private void initTestData() {
        ITestRootResource testDataResource = getTestDataResource();
        testData = testDataResource.init(getContentProviderName(), isLoanPerProduct());
        log.info("Test data initialized: " + testData.toString());
    }

    private void deleteTestData() {
        ITestRootResource testDataResource = getTestDataResource();
        jakarta.ws.rs.core.Response response = testDataResource.delete();
        if (response.getStatus() == jakarta.ws.rs.core.Response.Status.OK.getStatusCode()) {
            log.info("Test data deleted");
        } else {
            throw new IllegalStateException("Could not delete test data: " + response.getStatusInfo().getReasonPhrase());
        }
        log.info("Test data deleted");
    }

    private String getTestDataServiceBaseUri() {
        return System.getProperty(EHUB_SERVER_URI) + "/test";
    }

    private void setEhubServer() {
        System.setProperty(EHUB_SERVER_URI, "http://localhost:" + EHUB_PORT_NO + "/api");
        System.setProperty("catalina.base", "target");
    }

    private void setUpEhubClient() {
        @SuppressWarnings("resource") ApplicationContext springContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        underTest = IEhubService.class.cast(springContext.getBean("ehubClient"));
    }

    private void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder().ehubConsumerId(testData.getEhubConsumerId()).patron(getPatron()).build();
    }

    private ITestRootResource getTestDataResource() {
        final LoggingFeature loggingFeature = new LoggingFeature();
        final JsonProvider jsonProvider = new JsonProvider();
        final String baseUrl = getTestDataServiceBaseUri();
        return JAXRSClientFactory.create(baseUrl, ITestRootResource.class, Collections.singletonList(jsonProvider), Collections.singletonList(loggingFeature), null);
    }

    protected void thenExpectedContentProviderErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new ContentProviderErrorExceptionMatcher(EhubException.class, getContentProviderName(), status)));
    }


    protected void thenExpectedLmsErrorException(final Exception exception, final String status) {
        assertThat(exception, Matchers.is(new LmsErrorExceptionMatcher(EhubException.class, testData.getEhubConsumerId(), status)));
    }

    protected void thenExpectedEhubException(final Exception exception, final Class clazz, final ErrorCause errorCause, final ErrorCauseArgument... arguments) {
        assertThat(exception, Matchers.is(new EhubExceptionMatcher(EhubException.class, errorCause, arguments)));
    }

    protected String getContentProviderAlias() {
        return TestDataConstants.CONTENT_PROVIDER_ALIAS_PREFIX + getContentProviderName();
    }


    protected Patron getPatron() {
        Patron.Builder patronBuilder = new Patron.Builder().libraryCard(testData.getLibraryCard()).pin(testData.getPin()).id(testData.getPatronId()).email(testData.getEmail()).name(testData.getName()).birthDate(testData.getBirthDate());
        return patronBuilder.build();
    }
}
