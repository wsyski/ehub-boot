package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.provider.ep.IEpResource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("v5.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ILppEpResource extends IEpResource<LppCheckoutDTO,LppCheckoutRequestDTO> {

    @POST
    @Path("checkouts")
    LppCheckoutDTO checkout(LppCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LppCheckoutDTO getCheckout(@PathParam("id") String checkoutId);

}
