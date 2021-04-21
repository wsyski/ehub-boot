package com.axiell.ehub.lms.arena.resources.patrons;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IPatronsResource {

    @Path("emedia")
    IPatronEmediaResource getEmediaResource();
}


