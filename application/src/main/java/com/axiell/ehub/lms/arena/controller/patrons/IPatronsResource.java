package com.axiell.ehub.lms.arena.controller.patrons;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IPatronsResource {

    @Path("emedia")
    IPatronEmediaResource getEmediaResource();
}


