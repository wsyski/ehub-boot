package com.axiell.ehub.provider.ocd;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IOcdResource {

    @GET
    @Path("v1/libraries/{libraryId}/media")
    List<MediaDTO> getAllMedia(@HeaderParam("Authorization") BasicToken basicToken, @PathParam("libraryId") String libraryId);

    @POST
    @Path("v1/libraries/{libraryId}/patrons")
    PatronDTO addPatron(@HeaderParam("Authorization") BasicToken basicToken, @PathParam("libraryId") String libraryId, PatronDTO patronDTO);

    @POST
    @Path("v1/tokens")
    BearerToken newBearerToken(@HeaderParam("Authorization") BasicToken basicToken, PatronTokenDTO patronTokenDTO);

    @POST
    @Path("v1/transactions/checkouts/{isbn}")
    CheckoutDTO checkout(@HeaderParam("Authorization") BearerToken bearerToken, @HeaderParam("Accept-Media") String acceptMedia, @PathParam("isbn") String contentProviderRecordId);

    @DELETE
    @Path("v1/transactions/checkouts/{transactionId}")
    void checkin(@HeaderParam("Authorization") BearerToken bearerToken, @PathParam("transactionId") String contentProviderLoanId);

    @GET
    @Path("v1/transactions/checkouts/{transactionId}")
    List<CheckoutDTO> getCheckout(@HeaderParam("Authorization") BearerToken bearerToken, @PathParam("transactionId") String contentProviderLoanId);

    @GET
    @Path("v1/transactions/checkouts/all")
    List<CheckoutDTO> getCheckouts(@HeaderParam("Authorization") BearerToken bearerToken);
}
