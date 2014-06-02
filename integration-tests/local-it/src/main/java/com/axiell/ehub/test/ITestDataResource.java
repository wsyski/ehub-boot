package com.axiell.ehub.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("v1/test-data")
public interface ITestDataResource {

    @POST
    TestData init();

    @DELETE
    Response delete();
}
