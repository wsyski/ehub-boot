/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.it;

import com.axiell.ehub.EhubException;
import com.axiell.ehub.IEhubService;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.test.ITestDataResource;
import com.axiell.ehub.test.TestData;
import com.axiell.ehub.test.TestDataConstants;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

public abstract class RemoteITFixture {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteITFixture.class);
    private static final int PORT_NO = 16518;
    private static final String EHUB_SERVER_URI = "axiell-server-uri";
    protected static final String CONTENT_PROVIDER_ALIAS = "Distribut\u00f6r: " + TestDataConstants.CONTENT_PROVIDER_TEST_EP;

    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected TestData testData;
    protected AuthInfo authInfo;
    protected IEhubService underTest;

    @Rule
    public WireMockRule httpMockRule = new WireMockRule(16521);

    @Before
    public void setUp() throws EhubException {
        setEhubServerUri();
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
        testData = testDataResource.init();
        LOGGER.info("Test data initialized: " + testData.toString());
    }

    private void deleteTestData() {
        ITestDataResource testDataResource = getTestDataResource();
        testDataResource.delete();
        LOGGER.info("Test data deleted");
    }

    private String getTestDataServiceBaseUri() {
        return System.getProperty(EHUB_SERVER_URI);
    }

    private void setEhubServerUri() {
        System.setProperty(EHUB_SERVER_URI, "http://localhost:" + PORT_NO);
    }

    private void setUpEhubClient() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/remote-client-context.xml"});
        underTest = IEhubService.class.cast(springContext.getBean("ehubClient"));
    }

    private void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey()).libraryCard(testData.getLibraryCard())
                .pin(testData.getPin()).build();
    }

    private ITestDataResource getTestDataResource() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(getTestDataServiceBaseUri());
        return target.proxy(ITestDataResource.class);
    }
}
