== Spring Boot - Samples - CXF Rest Web Services

This sample project demonstrates how to use CXF JAX-RS services
with Spring Boot and Spring Actuator. This demo has two JAX-RS class resources being deployed
in a single JAX-RS endpoint.

= Starting the server =

The sample uses Maven. It can be built and run from the command line using Maven, Java or Docker:

---- With Maven ----

$ mvn -pl application -Pserver

---- With Java ----

$ java -jar target/spring-boot-sample-rs-cxf.jar


= Testing the server =

---- From the browser ----

http://localhost:16518/api/v5.0/hello/sayHello/ApacheCxfUser

will display "Hello ApacheCxfUser, Welcome to CXF RS Spring Boot World!!!"

http://localhost:16518/api/openapi.json will return a Swagger JSON
description of services.

To view the OpenAPI document using Swagger-UI, use your browser to
open the Swagger-UI page at

  http://localhost:16518/api/api-docs?url=/api/openapi.json

or access it from the CXF Services page:

  http://localhost:16518/api/services
  and follow a Swagger link.

To view the exposed metrics:
  http://localhost:16518/actuator/metrics

The Apache CXF specific metrics are available under:
  http://localhost:16518/actuator/metrics/cxf.server.requests

---- From the command line ----

--
$ mvn verify -pl application -Pintegration-test
--

Links:
https://dev.to/rodnan-sol/testing-with-maven-organizing-unit-and-integration-tests-35oh
https://github.com/rodnansol/articles-posts/blob/main/Testing-with-Maven/parent-module/simple-test-setup/pom.xml
https://github.com/dmakariev/examples
https://github.com/ityouknow/spring-boot-examples
https://github.com/gyandarpan22/Spring-Boot-Tutorial


