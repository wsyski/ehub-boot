/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import com.axiell.ehub.test.TestData;
import org.eclipse.jetty.server.handler.ContextHandler.Context;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.security.AuthInfo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public abstract class AbstractRemoteIT<D extends DevelopmentData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRemoteIT.class);
    private static final int PORT_NO = 9080;
    private static final String EHUB_SERVER_URI = "ehub-server-uri";
    protected static final String CONTENT_PROVIDER_NAME = ContentProviderName.ELIB.toString();
    protected TestData testData;
    protected AuthInfo authInfo;

    @Rule
    public WireMockRule httpMockRule = new WireMockRule(16521);

    protected abstract D initDevelopmentData(XmlWebApplicationContext applicationContext, IContentProviderAdminController contentProviderAdminController,
                                             IFormatAdminController formatAdminController, IConsumerAdminController consumerAdminController);

    @Before
    public void setUp() throws Exception {
        setEhubServerUri();
        setUpEhubClient();
        initTestData();
        initAuthInfo();
    }

    private void initTestData() throws Exception {
        final ClientRequest request = new ClientRequest(getTestDataServiceBaseUri());
        final ClientResponse<TestData> response = request.post(TestData.class);
        testData = response.getEntity();
    }

    private String getTestDataServiceBaseUri() {
        return System.getProperty(EHUB_SERVER_URI) + "/test-data";
    }

    private void setEhubServerUri() {
        System.setProperty(EHUB_SERVER_URI, "http://localhost:" + PORT_NO);
    }

    private void setUpEhubClient() {
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/remote-client-context.xml"});
        Object bean = springContext.getBean("ehubClient");
        castBeanToIEhubService(bean);
    }

    protected abstract void castBeanToIEhubService(Object bean);

    private void initAuthInfo() throws EhubException {
        authInfo = new AuthInfo.Builder(testData.getEhubConsumerId(), testData.getEhubConsumerSecretKey())
                .libraryCard(testData.getLibraryCard()).pin(testData.getPin()).build();
        LOGGER.debug("Authorization header value = " + authInfo.toString());
    }

    @After
    public void tearDown() throws Exception {
        final ClientRequest request = new ClientRequest(getTestDataServiceBaseUri());
        final ClientResponse<?> response = request.delete();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            final String reason = response.getEntity(String.class);
            throw new IllegalStateException("Could not delete test data: " + reason);
        }
    }
}
