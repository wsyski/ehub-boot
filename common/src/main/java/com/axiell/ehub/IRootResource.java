package com.axiell.ehub;

import com.axiell.ehub.checkout.ICheckoutsResource;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.security.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRootResource {

    @GET
    Response root();

    @Path("content-providers")
    IContentProvidersResource contentProviders(@HeaderParam("Authorization") AuthInfo authInfo);

    @Path("checkouts")
    ICheckoutsResource checkouts(@HeaderParam("Authorization") AuthInfo authInfo);
}
