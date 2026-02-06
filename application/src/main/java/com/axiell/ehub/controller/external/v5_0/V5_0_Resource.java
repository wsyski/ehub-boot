package com.axiell.ehub.controller.external.v5_0;

import com.axiell.ehub.controller.external.v5_0.hello.HelloResource;
import com.axiell.ehub.controller.external.v5_0.hello.IHelloResource;

public class V5_0_Resource implements IV5_0_Resource {

    @Override
    public IHelloResource getHelloResource() {
        return new HelloResource();
    }
}
