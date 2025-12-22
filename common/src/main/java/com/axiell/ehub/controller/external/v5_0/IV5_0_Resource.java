package com.axiell.ehub.controller.external.v5_0;

import com.axiell.ehub.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.provider.IContentProvidersResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IV5_0_Resource {

    @Path("content-providers")
    IContentProvidersResource contentProviders();

    @Path("checkouts")
    ICheckoutsResource checkouts();
}
