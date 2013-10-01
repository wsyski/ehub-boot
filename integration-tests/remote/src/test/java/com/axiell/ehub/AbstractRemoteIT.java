/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import javax.sql.DataSource;

import org.eclipse.jetty.server.handler.ContextHandler.Context;
import org.eclipse.jetty.webapp.WebAppContext;
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
    protected static final String CONTENT_PROVIDER_NAME = ContentProviderName.ELIB.toString();
    private ServiceLauncher serviceLauncher;
    private XmlWebApplicationContext applicationContext;
    protected D developmentData;
    protected AuthInfo authInfo;

    @Rule
    public WireMockRule palmaMockRule = new WireMockRule(16521);
    @Rule
    public WireMockRule elibMockRule = new WireMockRule(16530);

    protected abstract D initDevelopmentData(XmlWebApplicationContext applicationContext, IContentProviderAdminController contentProviderAdminController,
	    IFormatAdminController formatAdminController, IConsumerAdminController consumerAdminController);

    @Before
    public void setUp() throws Exception {
	setEhubServerUri();	
	setUpEhubClient();
	
	WebAppContext webAppContext = startService();
	initApplicationContext(webAppContext);

	initDevelopmentData();
	initAuthInfo();
    }

    private WebAppContext startService() throws Exception {
	serviceLauncher = new ServiceLauncher(PORT_NO, ".", "../../local/src/main");
	WebAppContext webAppContext = serviceLauncher.addWebContext(".", "", "../../local/src/test/resources/");
	serviceLauncher.start();
	return webAppContext;
    }

    private void initApplicationContext(WebAppContext webAppContext) {
	Context servletContext = webAppContext.getServletContext();
	applicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    private void setEhubServerUri() {
	System.setProperty("ehub-server-uri", "http://localhost:" + PORT_NO);
    }

    private void setUpEhubClient() {
	@SuppressWarnings("resource")
	ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[] { "/com/axiell/ehub/remote-client-context.xml" });
	Object bean = springContext.getBean("ehubClient");
	castBeanToIEhubService(bean);
    }
    
    protected abstract void castBeanToIEhubService(Object bean);
    
    private void initDevelopmentData() throws Exception {
	IContentProviderAdminController contentProviderAdminController = applicationContext.getBean(IContentProviderAdminController.class);
	IFormatAdminController formatAdminController = applicationContext.getBean(IFormatAdminController.class);
	IConsumerAdminController consumerAdminController = applicationContext.getBean(IConsumerAdminController.class);

	developmentData = initDevelopmentData(applicationContext, contentProviderAdminController, formatAdminController, consumerAdminController);
	developmentData.init();
    }

    private void initAuthInfo() throws EhubException {
	authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
		.libraryCard(DevelopmentData.ELIB_LIBRARY_CARD).pin(DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
	LOGGER.debug("Authorization header value = " + authInfo.toString());
    }

    @After
    public void tearDown() throws Exception {
	DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
	developmentData.destroy(dataSource);
	serviceLauncher.stop();
    }
}
