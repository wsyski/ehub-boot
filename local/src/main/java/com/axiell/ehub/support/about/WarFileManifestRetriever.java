package com.axiell.ehub.support.about;

import org.apache.wicket.protocol.http.WebApplication;

import javax.servlet.ServletContext;

class WarFileManifestRetriever {

    private WarFileManifestRetriever() {
    }

    static WarFileManifest retrieve() {
        final ServletContext servletContext = WebApplication.get().getServletContext();
        return WarFileManifest.read(servletContext);
    }
}
