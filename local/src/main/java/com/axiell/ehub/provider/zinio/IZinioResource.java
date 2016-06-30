package com.axiell.ehub.provider.zinio;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public interface IZinioResource {
    static final String CMD_P_ACCOUNT_CREATE = "p_account_create";
    static final String CMD_P_EXISTS = "p_exists";
    static final String CMD_P_LOGIN = "p_login";
    static final String CMD_ZINIO_CHECKOUT_ISSUE = "zinio_checkout_issue";
    static final String CMD_ZINIO_ISSUES_BY_MAGAZINES_AND_LIBRARY = "zinio_issues_by_magazines_and_library";


    @POST
    @Path("api")
    String addPatron(@DefaultValue(CMD_P_ACCOUNT_CREATE) @FormParam("cmd") String cmd, @FormParam("lib_id") String lib_id, @FormParam("token") String token,
                     @FormParam("email") String email, @FormParam("pwd") String pwd, @FormParam("pwd2") String pwd2, @FormParam("name") String name,
                     @FormParam("last_name") String last_name, @FormParam("barcode") String barcode);

    @GET
    @Path("api")
    String patronExists(@DefaultValue(CMD_P_EXISTS) @QueryParam("cmd") String cmd, @QueryParam("lib_id") String lib_id, @QueryParam("token") String token,
                        @QueryParam("email") String email);

    @POST
    @Path("api")
    String login(@DefaultValue(CMD_P_LOGIN) @FormParam("cmd") String cmd, @FormParam("lib_id") String lib_id, @FormParam("token") String token,
                 @FormParam("email") String email);

    @POST
    @Path("api")
    String checkout(@DefaultValue(CMD_ZINIO_CHECKOUT_ISSUE) @FormParam("cmd") String cmd, @FormParam("lib_id") String lib_id, @FormParam("token") String token,
                    @FormParam("email") String email, @FormParam("zinio_issue_id") String zinio_issue_id);

    @GET
    @Path("api")
    String getIssues(@DefaultValue(CMD_ZINIO_ISSUES_BY_MAGAZINES_AND_LIBRARY) @QueryParam("cmd") String cmd, @QueryParam("lib_id") String lib_id,
                     @QueryParam("token") String token, @QueryParam("zinio_magazine_rbid") String zinio_magazine_rbid);
}
