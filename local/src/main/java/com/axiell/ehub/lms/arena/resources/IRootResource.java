package com.axiell.ehub.lms.arena.resources;


import com.axiell.ehub.lms.arena.resources.patrons.IPatronsResource;
import com.axiell.ehub.lms.arena.resources.portalsites.IPortalSitesResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1")
public interface IRootResource {

    @GET
    Response root();

    @Path("patrons")
    IPatronsResource getPatronsResource();

    @Path("portalsites")
    IPortalSitesResource getPortalSitesResource();

}
