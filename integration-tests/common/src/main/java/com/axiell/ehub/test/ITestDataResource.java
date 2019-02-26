package com.axiell.ehub.test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v3")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ITestDataResource {

    @POST
    @Path("test-data")
    TestDataDTO init(@DefaultValue("TEST_EP") @QueryParam("contentProviderName") String contentProviderName,
                     @DefaultValue("false") @QueryParam("isLoanPerProduct") boolean isLoanPerProduct);

    @DELETE
    @Path("test-data")
    Response delete();
}
