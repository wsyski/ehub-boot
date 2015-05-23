package com.axiell.ehub.provider.ocd;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class OcdDownloadUrlResolver {
    public static String resolve(final String url) {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        DownloadUrlDTO downloadUrlDTO = response.readEntity(DownloadUrlDTO.class);
        response.close();
        return downloadUrlDTO.getDownloadUrl();
    }
}
