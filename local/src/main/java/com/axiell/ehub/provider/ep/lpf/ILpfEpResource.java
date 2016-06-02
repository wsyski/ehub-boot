package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.RecordDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ILpfEpResource {

    @POST
    @Path("checkouts")
    LpfCheckoutDTO checkout(LpfCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LpfCheckoutDTO getCheckout(@PathParam("id") String checkoutId);

    @GET
    @Path("records/{id}")
    RecordDTO getRecord(@PathParam("id") String recordId);
}
