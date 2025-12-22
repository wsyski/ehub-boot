package com.axiell.ehub.lms.arena.controller.patrons;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.lms.arena.exception.CheckedArenaException;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutTestEmediaResponseDTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface IPatronEmediaResource {

    @GET
    @Path("/records/{recordId}/providers/{contentProviderName}/formats/{formatId}/checkouts/test")
    CheckoutTestEmediaResponseDTO checkoutEmediaTest(
            @HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo,
            @PathParam("recordId") String recordId,
            @PathParam("contentProviderName") String contentProviderName,
            @PathParam("formatId") String formatId,
            @QueryParam("issue") String issue,
            @QueryParam("isLoanPerProduct") @DefaultValue("false") Boolean isLoanPerProduct,
            @QueryParam("origin") String origin) throws CheckedArenaException;

    @POST
    @Path("/records/{recordId}/providers/{contentProviderName}/formats/{formatId}/checkouts")
    @Produces({MediaType.APPLICATION_JSON})
    CheckoutEmediaResponseDTO checkoutEmedia(
            @HeaderParam(HttpHeaders.AUTHORIZATION) AuthInfo authInfo,
            @PathParam("recordId") String recordId,
            @PathParam("contentProviderName") String contentProviderName,
            @PathParam("formatId") String formatId,
            @QueryParam("origin") String origin,
            CheckoutEmediaRequestDTO checkoutEmediaRequestDTO) throws CheckedArenaException;

}
