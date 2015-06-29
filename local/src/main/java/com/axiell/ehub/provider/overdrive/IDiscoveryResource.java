package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1")
interface IDiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("libraries/{libraryId}")
    LibraryAccount getLibraryAccount(@HeaderParam("Authorization") OAuthAccessToken accessToken, @HeaderParam("X-Forwarded-For") String hostAddress,
	    @PathParam("libraryId") String libraryId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products/{productId}")
    ProductDTO getProduct(@HeaderParam("Authorization") OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
	    @PathParam("productId") String productId);
}
