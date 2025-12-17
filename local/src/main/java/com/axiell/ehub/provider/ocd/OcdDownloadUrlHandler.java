package com.axiell.ehub.provider.ocd;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class OcdDownloadUrlHandler {

    static String resolve(final String url) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();

        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            DownloadUrlDTO downloadUrlDTO = response.readEntity(DownloadUrlDTO.class);
            response.close();
            return downloadUrlDTO.getDownloadUrl();
        } else {
            throw new ClientErrorException(response);
        }
    }
}
