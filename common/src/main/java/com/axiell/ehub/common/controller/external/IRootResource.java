package com.axiell.ehub.common.controller.external;

import com.axiell.ehub.common.controller.external.v5_0.IV5_0_Resource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Service
public interface IRootResource {

    @Path("v5.0")
    IV5_0_Resource getIV5_0_Resource();
}
