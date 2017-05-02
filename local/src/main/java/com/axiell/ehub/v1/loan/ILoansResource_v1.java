/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.v1.loan;

import com.axiell.authinfo.AuthInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("v1/loans")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public interface ILoansResource_v1 {

    @POST
    ReadyLoan_v1 createLoan(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @DefaultValue("en") @QueryParam("language") String language, PendingLoan_v1 pendingLoan);

    @GET
    @Path("{id}")
    ReadyLoan_v1 getLoan(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @PathParam("id") Long readyLoanId, @DefaultValue("en") @QueryParam("language") String language);

    @GET
    ReadyLoan_v1 getLoan(@HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo, @QueryParam("lmsLoanId") String lmsLoanId, @DefaultValue("en") @QueryParam("language") String language);
}
