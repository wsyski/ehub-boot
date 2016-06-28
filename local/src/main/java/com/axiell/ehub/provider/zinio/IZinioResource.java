package com.axiell.ehub.provider.zinio;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IZinioResource {

    @POST
    @Path("api")
    String addPatron(@FormParam("cmd") String cmd, @FormParam("lib_id") String lib_id, @FormParam("token") String token, @FormParam("email") String email,
                     @FormParam("pwd") String pwd,
                     @FormParam("pwd2") String pwd2, @FormParam("name") String name, @FormParam("last_name") String last_name,
                     @FormParam("barcode") String barcode);

    @GET
    @Path("api")
    String patronExists(@QueryParam("cmd") String cmd, @QueryParam("lib_id") String lib_id, @QueryParam("token") String token,
                        @FormParam("email") String email);
}
