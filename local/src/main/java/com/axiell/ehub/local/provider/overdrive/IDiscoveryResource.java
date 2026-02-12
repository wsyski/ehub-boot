package com.axiell.ehub.local.provider.overdrive;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

@Path("/v1")
interface IDiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("libraries/{libraryId}")
    LibraryAccount getLibraryAccount(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken accessToken, @HeaderParam("X-Forwarded-For") String hostAddress,
                                     @PathParam("libraryId") String libraryId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products/{productId}")
    ProductDTO getProductById(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
                              @PathParam("productId") String productId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collections/{collectionToken}/products")
    ProductsDTO getProductsByCrossRefId(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAccessToken accessToken, @PathParam("collectionToken") String collectionToken,
                                        @QueryParam("crossRefId") String crossRefId);
}
