package com.axiell.ehub.provider.borrowbox;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IBorrowBoxResource {
    @GET
    @Path("v1/products/{recordId}")
    FormatsDTO getFormats(@HeaderParam("Authorization") AuthorizationToken authorizationToken, @HeaderParam("X-BorrowBox-Site") String libraryId,
                          @HeaderParam("X-BorrowBox-User") String libraryCard, @PathParam("recordId") String recordId);

    @POST
    @Path("v1/checkout")
    CheckoutDTO checkout(@HeaderParam("Authorization") AuthorizationToken authorizationToken, @HeaderParam("X-BorrowBox-Site") String libraryId,
                                 @HeaderParam("X-BorrowBox-User") String libraryCard, CheckoutRequestDTO checkoutRequest);

    @GET
    @Path("v1/loans/{loanId}")
    CheckoutDTO getCheckout(@HeaderParam("Authorization") AuthorizationToken authorizationToken, @HeaderParam("X-BorrowBox-Site") String libraryId,
                                    @HeaderParam("X-BorrowBox-User") String libraryCard, @PathParam("loanId") String loanId);

}
