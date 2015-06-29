package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/v1")
interface IAvailabilityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products/{productId}/availability")
    AvailabilityDTO getAvailability(@HeaderParam("Authorization") OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
                       @PathParam("productId") String productId);
}
