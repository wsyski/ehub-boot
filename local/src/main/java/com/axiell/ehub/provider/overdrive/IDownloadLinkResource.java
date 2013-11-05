package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

interface IDownloadLinkResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DownloadLink getDownloadLink(@HeaderParam("Authorization") OAuthAccessToken patronAccessToken);
}
