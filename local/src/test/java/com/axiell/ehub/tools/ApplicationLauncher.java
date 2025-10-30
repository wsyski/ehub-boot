package com.axiell.ehub.tools;

import oracle.jdbc.datasource.OracleDataSource;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationLauncher {
    private static final int PORT_NO = 16518;

    public static void main(String[] args) throws Exception {
        setSystemProperties();

        Server server = new Server();

        //Enable parsing of jndi-related parts of web.xml and jetty-env.xml
        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
        classlist.addAfter(org.eclipse.jetty.webapp.FragmentConfiguration.class.getName(),
                org.eclipse.jetty.plus.webapp.EnvConfiguration.class.getName(),
                org.eclipse.jetty.plus.webapp.PlusConfiguration.class.getName());
        // classlist.addBefore(org.eclipse.jetty.webapp.JettyWebXmlConfiguration.class.getName());
        ServerConnector connector = new ServerConnector(server);
        connector.setSoLingerTime(-1);
        connector.setPort(PORT_NO);
        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);

        webAppContext.setContextPath("/");
        // webAppContext.setExtraClasspath("target/classes");
        webAppContext.setWar("src/main/webapp");
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        server.setHandler(handlers);

        OracleDataSource dataSource = new oracle.jdbc.pool.OracleDataSource();
        dataSource.setURL(System.getProperty("hibernate.connection.url"));
        dataSource.setUser(System.getProperty("hibernate.connection.username"));
        dataSource.setPassword(System.getProperty("hibernate.connection.password"));

        new org.eclipse.jetty.plus.jndi.Resource(webAppContext, "jdbc/ehub", dataSource);

        try {
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(10000);
            }
            server.stop();
            server.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private static Properties getHibernateProperties() {

        final Properties hibernateProperties = new Properties();
        try {
            hibernateProperties.load(new FileInputStream("src/main/resources/hibernate.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        final Properties systemProperties = new Properties();
        systemProperties.setProperty("hibernate.connection.username", hibernateProperties.getProperty("hibernate.connection.username"));
        systemProperties.setProperty("hibernate.connection.password", hibernateProperties.getProperty("hibernate.connection.password"));
        systemProperties.setProperty("hibernate.connection.url", hibernateProperties.getProperty("hibernate.connection.url"));

        return systemProperties;
    }

    private static void setSystemProperties() throws IOException {
        // System.setProperty("catalina.base", "src/main");
        final Properties systemProperties = getHibernateProperties();
        System.getProperties().putAll(systemProperties);
    }
}
