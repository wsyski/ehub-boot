package com.axiell.ehub.provider.sdk;

import com.axiell.ehub.FieldsDTO;
import com.axiell.ehub.security.AuthInfo;

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
    CheckoutDTO checkout(@HeaderParam("Authorization") AuthInfo authInfo, CheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    CheckoutDTO getCheckout(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") Long ehubCheckoutId);

    @Path("records/{recordId}/formats")
    FormatsDTO records(@PathParam("recordId") String recordId);
}
