package com.axiell.ehub.provider.ocd;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.core.Response;

public class OcdDownloadUrlResolver {
    public static String resolve(final String url) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);
        Response response = target.request().get();
        DownloadUrlDTO downloadUrlDTO = response.readEntity(DownloadUrlDTO.class);
        response.close();
        return downloadUrlDTO.getDownloadUrl();
    }
}
