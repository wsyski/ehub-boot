package com.axiell.ehub.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test-data")
public interface ITestDataResource {

    @POST
    TestData init();

    @DELETE
    Response delete();
}
