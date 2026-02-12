package com.axiell.ehub.local.provider.ep;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

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
