package com.axiell.ehub.controller.external;

import com.axiell.ehub.controller.external.v5_0.IV5_0_Resource;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IRootResource {

    @GET
    Response root();

    @Path("v5.0")
    IV5_0_Resource getIV5_0_Resource();
}
