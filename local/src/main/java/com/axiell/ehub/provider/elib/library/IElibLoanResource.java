/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.elib.library.loan.Response;

/**
 * Defines the loan parts of the Elib Library uniform interface.
 */
public interface IElibLoanResource {

    /**
     * Creates a loan at Elib.
     * 
     * @param retailerId the retailer ID
     * @param md5RetailerKeyCode the MD5 hash of the retailer key code
     * @param contentProviderRecordId the ID of the record at Elib
     * @param formatId the format of the record at Elib
     * @param libraryCard the library card identifying the user
     * @param pin the pin
     * @param language the language the response should be in
     * @param dummyMobiPocketId a required parameter that isn't used
     * @return a {@link Response} containing the Elib loan
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_XML)
    Response createLoan(@FormParam("retailerid") String retailerId,
            @FormParam("retailerkeycode") String md5RetailerKeyCode,
            @FormParam("ebookid") String contentProviderRecordId,
            @FormParam("format") String formatId,
            @FormParam("cardnumber") String libraryCard,
            @FormParam("pincode") String pin,
            @FormParam("languagecode") String language,
            @FormParam("mobipocketid") String dummyMobiPocketId);
}
