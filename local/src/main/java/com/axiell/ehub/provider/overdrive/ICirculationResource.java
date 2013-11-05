package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/patrons/me/checkouts")
interface ICirculationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Checkouts getCheckouts(@HeaderParam("Authorization") OAuthAccessToken patronAccessToken);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Checkout checkout(@HeaderParam("Authorization") OAuthAccessToken patronAccessToken, Fields fields);
    
    @DELETE
    @Path("{productId}")
    void returnTitle(@HeaderParam("Authorization") OAuthAccessToken patronAccessToken, @PathParam("productId") String productId);
}
