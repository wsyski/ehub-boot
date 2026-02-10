package com.axiell.ehub.test;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ITestRootResource {

    @POST
    @Path("v5.0/data")
    TestDataDTO init(@DefaultValue("TEST_EP") @QueryParam("contentProviderName") String contentProviderName,
                     @DefaultValue("false") @QueryParam("isLoanPerProduct") boolean isLoanPerProduct);

    @DELETE
    @Path("v5.0/data")
    Response delete();
}
