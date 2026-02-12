package com.axiell.ehub.local.provider.ep.lpp;

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
public interface ILppEpResource extends IEpResource<LppCheckoutDTO, LppCheckoutRequestDTO> {

    @POST
    @Path("checkouts")
    LppCheckoutDTO checkout(LppCheckoutRequestDTO checkoutRequest);

    @GET
    @Path("checkouts/{id}")
    LppCheckoutDTO getCheckout(@PathParam("id") String checkoutId);

}
