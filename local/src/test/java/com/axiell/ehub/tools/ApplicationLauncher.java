package com.axiell.ehub.tools;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
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
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration",
                "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration");
        ServerConnector connector = new ServerConnector(server);
        connector.setSoLingerTime(-1);
        connector.setPort(PORT_NO);
        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);

        webAppContext.setContextPath("");
        webAppContext.setExtraClasspath("target/classes");
        webAppContext.setWar("src/main/webapp");
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        server.setHandler(handlers);

        String[] configFileNames = {"config/jetty-env.xml"};
        for (String configFileName : configFileNames) {
            File configFile = new File(configFileName);
            if (configFile.exists() && configFile.isFile()) {
                XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(configFile));
                configuration.configure(server);
            } else {
                System.err.println("Missing file: " + configFileName);
            }
        }

        // START JMX SERVER
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();
        //server.addHandler(portletContext);

        try {
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(10000);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }

    private static void setSystemProperties() throws IOException {
        System.setProperty("catalina.base", "src/main");
        final Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/hibernate.properties"));
        setSystemProperty(properties, "hibernate.connection.username");
        setSystemProperty(properties, "hibernate.connection.password");
        setSystemProperty(properties, "hibernate.connection.url");
    }

    private static void setSystemProperty(final Properties properties, final String key) {
        System.setProperty(key, properties.getProperty(key));
    }
}
