package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
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
