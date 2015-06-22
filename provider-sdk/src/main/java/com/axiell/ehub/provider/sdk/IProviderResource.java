package com.axiell.ehub.provider.sdk;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IProviderResource {

    @GET
    Response root();

    @POST
    @Path("checkouts")
    CheckoutDTO checkout(@HeaderParam("Authorization") Authorization authorization, CheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    CheckoutDTO getCheckout(@HeaderParam("Authorization") Authorization authorization, @PathParam("id") Long ehubCheckoutId);

    @Path("records/{recordId}/formats")
    FormatsDTO records(@HeaderParam("Authorization") Authorization authorization, @PathParam("recordId") String recordId);
}
