package com.axiell.ehub.provider.ocd;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IOcdResource {

    @GET
    @Path("v1/libraries/{libraryId}/media")
    List<MediaDTO> getAllMedia(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId);

    @GET
    @Path("v1/rpc/libraries/{libraryId}/patrons/{email}")
    PatronDTO getPatronByEmail(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId, @PathParam("email") String email);

    @POST
    @Path("v1/libraries/{libraryId}/patrons")
    PatronDTO addPatron(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId, PatronDTO patronDTO);

    @GET
    @Path("v1/libraries/{libraryId}/patrons")
    List<PatronDTO> getAllPatrons(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, @PathParam("libraryId") String libraryId);

    @POST
    @Path("v1/tokens")
    BearerToken newBearerToken(@HeaderParam(HttpHeaders.AUTHORIZATION) BasicToken basicToken, PatronTokenDTO patronTokenDTO);

    @POST
    @Path("v1/transactions/checkouts/{isbn}")
    CheckoutDTO checkout(@HeaderParam(HttpHeaders.AUTHORIZATION) BearerToken bearerToken, @HeaderParam("Accept-Media") String acceptMedia, @PathParam("isbn") String contentProviderRecordId);

    @DELETE
    @Path("v1/transactions/checkouts/{transactionId}")
    void checkin(@HeaderParam(HttpHeaders.AUTHORIZATION) BearerToken bearerToken, @PathParam("transactionId") String contentProviderLoanId);

    @GET
    @Path("v1/transactions/checkouts/{transactionId}")
    List<CheckoutDTO> getCheckout(@HeaderParam(HttpHeaders.AUTHORIZATION) BearerToken bearerToken, @PathParam("transactionId") String contentProviderLoanId);

    @GET
    @Path("v1/transactions/checkouts/all")
    List<CheckoutDTO> getCheckouts(@HeaderParam(HttpHeaders.AUTHORIZATION) BearerToken bearerToken);
}
