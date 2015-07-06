package com.axiell.ehub.test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ITestDataResource {

    @POST
    @Path("test-data")
    TestData init();

    @DELETE
    @Path("test-data")
    Response delete();
}
