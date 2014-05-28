/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import javax.ws.rs.*;
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
     * <p/>
     * <p>
     * Example path: <code>/loans</code>
     * </p>
     *
     * @param authInfo    an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @param pendingLoan a {@link PendingLoan}
     * @return a {@link ReadyLoan}
     */
    @POST
    ReadyLoan createLoan(@HeaderParam("Authorization") AuthInfo authInfo, @DefaultValue("en") @QueryParam("language") String language, PendingLoan pendingLoan);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and the ID of the ready loan.
     * <p/>
     * <p>
     * Example path: <code>/loans/987</code>
     * </p>
     *
     * @param authInfo    an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param readyLoanId the ID of the {@link ReadyLoan}
     * @param language    the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ReadyLoan}
     */
    @GET
    @Path("{id}")
    ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, @PathParam("id") Long readyLoanId, @DefaultValue("en") @QueryParam("language") String language);

    /**
     * Gets a {@link ReadyLoan} for the provided {@link AuthInfo} and LMS loan ID.
     * <p/>
     * <p>
     * Example path: <code>/loans?lmsLoanId=987</code>
     * </p>
     *
     * @param authInfo  an {@link AuthInfo} containing an eHUB consumer ID, library card and pin
     * @param lmsLoanId the ID of the loan in the LMS
     * @param language  the ISO 639 alpha-2 or alpha-3 language code, can be <code>null</code>
     * @return a {@link ReadyLoan}
     */
    @GET
    ReadyLoan getLoan(@HeaderParam("Authorization") AuthInfo authInfo, @QueryParam("lmsLoanId") String lmsLoanId, @DefaultValue("en") @QueryParam("language") String language);
}
