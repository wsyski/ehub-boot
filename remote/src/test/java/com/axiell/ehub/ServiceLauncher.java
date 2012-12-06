package com.axiell.ehub;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServiceLauncher {
    private Server server;
    private String catalinaHome;

    public ServiceLauncher(final Integer port, final String homePath, final String catalinaHome) {
        server = new Server(port);
        this.catalinaHome = catalinaHome;
        System.setProperty("catalina.home", homePath + '/' + catalinaHome);
    }

    public WebAppContext addWebContext(final String warPath, final String contextPath, final String extraClassPath) {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);
        webAppContext.setContextPath(contextPath);
        webAppContext.setWar(warPath + '/' + catalinaHome + "/webapp");
        if (extraClassPath != null) {
            webAppContext.setExtraClasspath(extraClassPath);
        }
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        server.setHandler(handlers);
        return webAppContext;
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
        server.join();
    }

}
