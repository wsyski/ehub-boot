package com.axiell.ehub.controller.external.v5_0.hello;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Tag(name = "hello", description = "Hello Service API")
@Service
public interface IHelloResource {

    @GET
    @Path("sayHello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    String sayHello(@PathParam("name") String name);

}
