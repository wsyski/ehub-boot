package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.provider.ep.IEpResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ILpfEpResource extends IEpResource<LpfCheckoutDTO,LpfCheckoutRequestDTO> {

    @POST
    @Path("checkouts")
    LpfCheckoutDTO checkout(LpfCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LpfCheckoutDTO getCheckout(@PathParam("id") String checkoutId);
}
