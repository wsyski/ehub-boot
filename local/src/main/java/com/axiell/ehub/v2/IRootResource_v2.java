package com.axiell.ehub.v2;

import com.axiell.ehub.v2.checkout.ICheckoutsResource_v2;
import com.axiell.ehub.v2.provider.IContentProvidersResource_v2;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRootResource_v2 {

    @GET
    Response root();

    @Path("content-providers")
    IContentProvidersResource_v2 contentProviders();

    @Path("checkouts")
    ICheckoutsResource_v2 checkouts();
}
