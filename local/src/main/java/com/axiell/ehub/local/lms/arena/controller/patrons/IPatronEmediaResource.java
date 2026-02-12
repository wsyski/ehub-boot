package com.axiell.ehub.local.lms.arena.controller.patrons;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.local.lms.arena.controller.patrons.dto.CheckoutTestEmediaResponseDTO;
import com.axiell.ehub.local.lms.arena.exception.CheckedArenaException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
