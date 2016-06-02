package com.axiell.ehub.provider.ep;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IEpResource {

    @GET
    @Path("records/{id}")
    RecordDTO getRecord(@PathParam("id") String recordId);
}
