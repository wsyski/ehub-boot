/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.IContentProviderAdminController;
import com.axiell.ehub.provider.record.format.IFormatAdminController;
import com.axiell.ehub.security.AuthInfo;

/**
 * 
 */
public abstract class AbstractEhubClientTest<D extends DevelopmentData> {
    private static final Logger LOGGER = Logger.getLogger(AbstractEhubClientTest.class);
    private static final int PORT_NO = 9080;

    @Resource(name = "ehubClient")
    protected IEhubService ehubService;

    private ServiceLauncher serviceLauncher;
    private XmlWebApplicationContext applicationContext;
    protected D developmentData;
    protected AuthInfo authInfoNoCard;
    protected AuthInfo authInfo;

    /**
     * 
     * @param applicationContext
     * @param contentProviderAdminController
     * @param formatAdminController
     * @param consumerAdminController
     * @return
     */
    protected abstract D initDevelopmentData(XmlWebApplicationContext applicationContext,
            IContentProviderAdminController contentProviderAdminController,
            IFormatAdminController formatAdminController,
            IConsumerAdminController consumerAdminController);

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[]{"/com/axiell/ehub/remote-client-context.xml"});
        ehubService = (IEhubService) springContext.getBean("ehubClient");

        assertNotNull(ehubService);
        serviceLauncher = new ServiceLauncher(PORT_NO, ".", "../local/src/main");
        WebAppContext webAppContext = serviceLauncher.addWebContext(".", "", "../local/src/test/resources/");
        serviceLauncher.start();
        applicationContext = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(webAppContext.getServletContext());
        IContentProviderAdminController contentProviderAdminController = applicationContext.getBean(IContentProviderAdminController.class);
        IFormatAdminController formatAdminController = applicationContext.getBean(IFormatAdminController.class);
        IConsumerAdminController consumerAdminController = applicationContext.getBean(IConsumerAdminController.class);
        developmentData = initDevelopmentData(applicationContext, contentProviderAdminController, formatAdminController, consumerAdminController);
        developmentData.init();
        authInfoNoCard = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY).build();
        authInfo = new AuthInfo.Builder(developmentData.getEhubConsumerId(), DevelopmentData.EHUB_CONSUMER_SECRET_KEY)
                .libraryCard(DevelopmentData.ELIB_LIBRARY_CARD).pin(DevelopmentData.ELIB_LIBRARY_CARD_PIN).build();
        LOGGER.debug("Authorization header value = " + authInfo.toString());
        
    }

    /**
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        developmentData.destroy(dataSource);
        serviceLauncher.stop();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEhubServicePing() throws Exception {
        URL url = new URL("http://localhost:" + PORT_NO + "/index.html");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
        assertTrue(IOUtils.toString(connection.getInputStream()).contains("Axiell eHUB"));
    }

    protected static boolean isOnline() {
        return System.getProperty("online") != null && Boolean.valueOf(System.getProperty("online"));
    }
}
