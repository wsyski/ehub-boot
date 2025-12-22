package com.axiell.ehub.tools;


import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class ApplicationLauncher {

    private static final int PORT_NO = 16518;

    public static void main(String[] args) throws Exception {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT_NO);
        server.addConnector(connector);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);

        webAppContext.setContextPath("/");
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

}
