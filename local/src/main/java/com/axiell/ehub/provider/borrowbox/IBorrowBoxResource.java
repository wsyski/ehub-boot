package com.axiell.ehub.provider.borrowbox;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1")
interface IBorrowBoxResource {
    @GET
    @Path("products/{recordId}")
    FormatsDTO getFormats(@PathParam("recordId") String recordId);

    @POST
    @Path("checkout")
    CheckoutDTO checkout(CheckoutRequestDTO checkoutRequest);

    @GET
    @Path("loans/{loanId}")
    CheckoutDTO getCheckout(@PathParam("loanId") String loanId);

}
