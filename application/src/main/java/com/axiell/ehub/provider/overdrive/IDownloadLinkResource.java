package com.axiell.ehub.provider.overdrive;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

interface IDownloadLinkResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DownloadLinkDTO getDownloadLink(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken);
}
