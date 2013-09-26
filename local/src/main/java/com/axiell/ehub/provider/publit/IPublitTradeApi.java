package com.axiell.ehub.provider.publit;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.cache.Cache;

@Path("/api")
public interface IPublitTradeApi {

    @GET
    @Cache
    @Path("/products/{isbn13}/ok")
    @Produces(APPLICATION_JSON)
    public List<Product> getProduct(@PathParam("isbn13")
    final String isbn13);

    @GET
    @Path("/download-ticket/{orderId}/ok")
    @Produces(APPLICATION_JSON)
    public ShopOrderUrl getShopOrderUrl(@PathParam("orderId")
    final String orderId);

    @POST
    @Path("/orders")
    @Produces(APPLICATION_JSON)
    public ShopCustomerOrder createShopOrder(@QueryParam("retailer_customer_id")
    final String customerId, @QueryParam("firstname")
    final String firstName, @QueryParam("lastname")
    final String lastName, @QueryParam("email")
    final String email, @QueryParam("street")
    final String street, @QueryParam("zipCode")
    final String zipCode, @QueryParam("city")
    final String city, @QueryParam("payment_method")
    final String paymentMethod, @QueryParam("order_type")
    final String orderType, @QueryParam("item_1")
    final String item, @QueryParam("callback")
    final String callback);

}
