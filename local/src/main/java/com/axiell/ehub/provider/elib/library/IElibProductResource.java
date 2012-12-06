/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.elib.library.product.Response;

/**
 * Defines the product parts of the Elib Library uniform interface.
 */
public interface IElibProductResource {

    /**
     * Gets a specific Elib product.
     * 
     * @param retailerId the retailer ID
     * @param md5RetailerKeyCode the MD5 hash of the retailer key code
     * @param contentProviderRecordId the ID of the record at Elib
     * @param language the language the response should be in
     * @return a {@link Response} containing an Elib product
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_XML)
    Response getProduct(@FormParam("retailerid") String retailerId,
            @FormParam("retailerkeycode") String md5RetailerKeyCode,
            @FormParam("ebookid") String contentProviderRecordId,
            @FormParam("languagecode") String language);
}
