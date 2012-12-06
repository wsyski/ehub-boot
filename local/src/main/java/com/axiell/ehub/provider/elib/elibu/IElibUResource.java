/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Defines the ElibU uniform interface.
 */
public interface IElibUResource {

    /**
     * Gets a specific product.
     * 
     * @param serviceId the service ID
     * @param md5ServiceKey the MD5 hash of the service key
     * @param elibuRecordId the ID of the record at ElibU
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{productid}")
    Response getProduct(@QueryParam("serviceid") String serviceId,
            @QueryParam("servicekey") String md5ServiceKey,
            @PathParam("productid") String elibuRecordId);

    /**
     * Consumes a license, i.e. either returns the existing license or creates a new one for the specific subscription
     * and library card.
     * 
     * <p>
     * See the <code>ConsumeLicense</code> section in the ElibU documentation for more information.
     * </p>
     * 
     * @param serviceId the service ID
     * @param md5ServiceKey the MD5 hash of the service key
     * @param subscriptionId the ID of the subscription to which the license belongs
     * @param libraryCard the library card to convert to a license
     * @return a {@link Response} containing a {@link License}
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    Response consumeLicense(@FormParam("serviceid") String serviceId,
            @FormParam("servicekey") String md5ServiceKey,
            @FormParam("subscriptionid") String subscriptionId,
            @FormParam("uniqueid") String libraryCard);

    /**
     * Consumes a product.
     * 
     * <p>
     * See the <code>ConsumeProduct</code> section in the ElibU documentation for more information.
     * </p
     * 
     * @param serviceId the service ID
     * @param md5ServiceKey the MD5 hash of the service key
     * @param elibuRecordId the ID of the record at ElibU
     * @param licenseId the ID of the license
     * @param md5Checksum the MD5 hash of the concatenated value of service ID, service key, license ID and ElibU record
     * ID
     * @return a {@link Response} containing an {@link ConsumedProduct}
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    Response consumeProduct(@FormParam("serviceid") String serviceId,
            @FormParam("servicekey") String md5ServiceKey,
            @FormParam("productid") String elibuRecordId,
            @FormParam("licenseid") Integer licenseId,
            @FormParam("checksum") String md5Checksum);
}
