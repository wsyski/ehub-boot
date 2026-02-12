package com.axiell.ehub.local.provider.overdrive;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

@Path("/v2")
interface IAvailabilityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products/{productId}/availability")
    AvailabilityDTO getAvailability(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
                                    @PathParam("productId") String productId);
}
