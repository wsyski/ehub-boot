package com.axiell.ehub.provider.generic;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IProviderResource {

    @POST
    @Path("checkouts")
    CheckoutDTO checkout(CheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    CheckoutDTO getCheckout(@PathParam("id") String checkoutId);

    @Path("records/{recordId}/formats")
    FormatsDTO getFormats(@PathParam("recordId") String recordId);
}
