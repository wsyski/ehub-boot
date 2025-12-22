package com.axiell.ehub.provider.overdrive;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/patrons/me/checkouts")
interface ICirculationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CheckoutsDTO getCheckouts(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{productId}/formats")
    CirculationFormatsDTO getCirculationFormats(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken, @PathParam("productId") String productId);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{productId}/formats")
    CirculationFormatDTO lockFormat(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken, @PathParam("productId") String productId, Fields fields);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    CheckoutDTO checkout(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken, Fields fields);

    @DELETE
    @Path("{productId}")
    void checkin(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken patronAccessToken, @PathParam("productId") String productId);


}
