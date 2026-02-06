/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.axiell.ehub.service;

import com.axiell.ehub.controller.EhubApplication;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EhubApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EhubApplicationTest {

    @LocalServerPort
    private int port;
    @Test
    public void testHelloRequest()  {
        WebClient wc = WebClient.create("http://localhost:" + port + "/api");
        wc.accept("text/plain");

        // HelloServiceImpl1
        wc.path("v5.0/hello/sayHello").path("wos");
        String greeting = wc.get(String.class);
        assertEquals("Hello wos, Welcome to CXF RS Spring Boot World!!!", greeting);

        // Reverse to the starting URI
        wc.back(true);
    }

}
