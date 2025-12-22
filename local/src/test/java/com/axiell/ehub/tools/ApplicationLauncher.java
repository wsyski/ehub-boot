package com.axiell.ehub.tools;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationLauncher {
    private static final int PORT_NO = 16518;

    public static void main(String[] args) throws Exception {
        setSystemProperties();

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT_NO);
        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        /*
        OracleDataSource dataSource = new oracle.jdbc.pool.OracleDataSource();
        dataSource.setURL(System.getProperty("connection.url"));
        dataSource.setUser(System.getProperty("connection.username"));
        dataSource.setPassword(System.getProperty("connection.password"));
        new org.eclipse.jetty.plus.jndi.Resource(webAppContext, "jdbc/ehub", dataSource);
        */
        webAppContext.setServer(server);

        webAppContext.setContextPath("/");
        webAppContext.setExtraClasspath("target/classes");
        webAppContext.setWar("src/main/webapp");
        server.setHandler(webAppContext);

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

    private static Properties getPersistenceProperties() {

        final Properties persistenceProperties = new Properties();
        try {
            persistenceProperties.load(new FileInputStream("src/main/resources/persistence.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        final Properties systemProperties = new Properties();
        systemProperties.setProperty("connection.username", persistenceProperties.getProperty("connection.username"));
        systemProperties.setProperty("connection.password", persistenceProperties.getProperty("connection.password"));
        systemProperties.setProperty("connection.url", persistenceProperties.getProperty("connection.url"));

        return systemProperties;
    }

    private static void setSystemProperties() throws IOException {
        // System.setProperty("catalina.base", "src/main");
        final Properties systemProperties = getPersistenceProperties();
        System.getProperties().putAll(systemProperties);
    }
}
