package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.ehub.local.provider.ep.IEpResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("v5.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ILpfEpResource extends IEpResource<LpfCheckoutDTO, LpfCheckoutRequestDTO> {

    @POST
    @Path("checkouts")
    LpfCheckoutDTO checkout(LpfCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LpfCheckoutDTO getCheckout(@PathParam("id") String checkoutId);
}
