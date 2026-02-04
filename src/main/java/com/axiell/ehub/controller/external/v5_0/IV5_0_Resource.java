package com.axiell.ehub.controller.external.v5_0;

import com.axiell.ehub.controller.external.v5_0.hello.IHelloResource;
import jakarta.ws.rs.Path;
import org.springframework.stereotype.Service;

@Service
public interface IV5_0_Resource {

    @Path("hello")
    IHelloResource getHelloResource();
}
