package com.axiell.ehub.provider.overdrive;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

interface IAccessTokenResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    OAuthAccessToken getAccessToken(@HeaderParam("Authorization") OAuthAuthorizationHeader authorizationHeader, @FormParam("grant_type") String grantType);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    OAuthAccessToken getPatronAccessToken(@HeaderParam("Authorization") OAuthAuthorizationHeader authorizationHeader,
	    @FormParam("grant_type") String grantType, @FormParam("username") String username, @FormParam("password") String password,
	    @FormParam("scope") Scope scope);
}
