package com.axiell.ehub.provider.ep;

import javax.ws.rs.*;

public interface IEpResource<C extends ICheckoutDTO, R extends ICheckoutRequestDTO> {

    @GET
    @Path("records/{id}")
    RecordDTO getRecord(@PathParam("id") String recordId);

    C checkout(R checkoutRequest);

    C getCheckout(String checkoutId);

    @DELETE
    @Path("checkouts/{id}")
    void deleteCheckout(@PathParam("id") String checkoutId);
}
