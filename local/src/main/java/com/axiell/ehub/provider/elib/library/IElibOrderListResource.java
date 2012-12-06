/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.elib.library.orderlist.Response;

/**
 * Defines the user order list parts of the Elib Library uniform interface.
 */
public interface IElibOrderListResource {

    /**
     * Gets an order list for a specific user.
     * 
     * @param retailerId the retailer ID
     * @param md5RetailerKeyCode the MD5 hash of the retailer key code
     * @param libraryCard the library card identifying the user
     * @param language the language the response should be in
     * @return a {@link Response} containing an order list
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_XML)
    Response getOrderList(@FormParam("retailerid") String retailerId,
            @FormParam("retailerkeycode") String md5RetailerKeyCode,
            @FormParam("cardnumber") String libraryCard,
            @FormParam("languagecode") String language);
}
