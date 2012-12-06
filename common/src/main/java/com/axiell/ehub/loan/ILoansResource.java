/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.security.AuthInfo;

/**
 * Defines the loan related resources that are accessible through the eHUB REST interface.
 */
@Path("v1/loans")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public interface ILoansResource {

    /**
     * Creates a loan both in the LMS and at the {@link ContentProvider}.
     * 
     * <p>
     * Example path: <code>/loans</code>
     * </p>
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param pendingLoan a {@link PendingLoan}
     * @return a {@link ReadyLoan}
     */
    @POST
    ReadyLoan createLoan(@HeaderParam("Authorization") AuthInfo authInfo, PendingLoan pendingLoan);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and the ID of the ready loan.
     * 
     * <p>
     * Example path: <code>/loans/987</code>
     * </p>
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param readyLoanId the ID of the {@link ReadyLoan}
     * @return a {@link ReadyLoan}
     */
    @GET
    @Path("{id}")
    ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") Long readyLoanId);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and LMS loan ID.
     * 
     * <p>
     * Example path: <code>/loans?lmsLoanId=987</code>
     * </p>
     * 
     * @param authInfo an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param lmsLoanId the ID of the loan in the LMS
     * @return a {@link ReadyLoan}
     */
    @GET
    ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, @QueryParam("lmsLoanId") String lmsLoanId);
}
