package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/v2")
interface IAvailabilityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products/{productId}/availability")
    AvailabilityDTO getAvailability(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
                                    @PathParam("productId") String productId);
}
