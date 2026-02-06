package com.axiell.ehub.controller.external.v5_0.hello;

public class HelloResource implements IHelloResource {

    public String sayHello(String name) {
        return "Hello " + name + ", Welcome to CXF RS Spring Boot World!!!";
    }
}
