package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.RecordDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ILppEpResource {

    @POST
    @Path("checkouts")
    LppCheckoutDTO checkout(LppCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LppCheckoutDTO getCheckout(@PathParam("id") String checkoutId);

    @GET
    @Path("records/{id}")
    RecordDTO getRecord(@PathParam("id") String recordId);
}
