package com.axiell.ehub.local.lms.arena.controller;


import com.axiell.ehub.local.lms.arena.controller.patrons.IPatronsResource;
import com.axiell.ehub.local.lms.arena.controller.portalsites.IPortalSitesResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v5.0")
public interface IArenaLocalRootResource {

    @GET
    Response root();

    @Path("patrons")
    IPatronsResource getPatronsResource();

    @Path("portalsites")
    IPortalSitesResource getPortalSitesResource();

}
