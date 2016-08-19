package com.axiell.ehub.tools;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class ApplicationLauncher {

    public static void main(String[] args) throws Exception {
        System.setProperty("catalina.base", "./src/main");
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setSoLingerTime(-1);
        connector.setPort(16518);
        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);
        webAppContext.setContextPath("");
        webAppContext.setWar("src/main/webapp");


        // START JMX SERVER
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        server.setHandler(handlers);

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(1000);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
