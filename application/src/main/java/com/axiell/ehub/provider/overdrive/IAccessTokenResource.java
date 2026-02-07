package com.axiell.ehub.provider.overdrive;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

interface IAccessTokenResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    OAuthAccessToken getAccessToken(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAuthorizationHeader authorizationHeader, @FormParam("grant_type") String grantType);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    OAuthAccessToken getPatronAccessToken(@HeaderParam(HttpHeaders.AUTHORIZATION) OAuthAuthorizationHeader authorizationHeader,
                                          @FormParam("grant_type") String grantType, @FormParam("username") String username, @FormParam("password") String password,
                                          @FormParam("password_required") boolean password_required, @FormParam("scope") Scope scope);
}
