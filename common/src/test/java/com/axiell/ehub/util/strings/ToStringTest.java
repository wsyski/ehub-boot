/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util.strings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse.BaseClientResponseStreamFactory;
import org.jboss.resteasy.specimpl.HttpHeadersImpl;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.spi.HttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ToStringTest {

    @Test
    public void testToString() throws UnsupportedEncodingException {
        Map<String, String> map1 = new LinkedHashMap<>();
        map1.put("oneKey", "oneValue");
        map1.put("twoKey", "twoValue");
        map1.put("threeKey", "threeValue");
        String toString = ToString.mapToString(map1);
        assertEquals("{oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}", toString);
        Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
        map2.put("oneMap", map1);
        map2.put("twoMap", map1);
        toString = ToString.mapToString(map2);
        assertEquals("{oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}",
                toString);
        Collection collection = Arrays.asList("col1", "col2", "col3");
        toString = ToString.collectionToString(collection);
        assertEquals("[col1, col2, col3]", toString);
        TestBean2 testBean2 = new TestBean2();
        testBean2.setField21(map2);
        testBean2.setField22(collection);
        TestBean1 testBean1 = new TestBean1();
        testBean1.setField11(map1);
        testBean1.setField12(collection);
        testBean1.setField13(testBean2);
        testBean1.setField14(new Date(0L));
        toString = ToString.objectToString(testBean1);
        assertEquals("[field11={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, " + SystemUtils.LINE_SEPARATOR +
                "  field12=[col1, col2, col3], " + SystemUtils.LINE_SEPARATOR +
                "  field13=[field21={oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}, " +
                SystemUtils.LINE_SEPARATOR +
                "  field22=[col1, col2, col3]], " + SystemUtils.LINE_SEPARATOR +
                "  field14=1970-01-01 01:00:00]", toString);
        Map<String, TestBean2> map3 = Collections.singletonMap("oneBean", testBean2);
        toString = ToString.mapToString(map3);
        assertEquals(
                "{oneBean=[field21={oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}, " +
                        SystemUtils.LINE_SEPARATOR +
                        "  field22=[col1, col2, col3]]}", toString);
    }

    private static class TestBean1 {
        Map field11;
        Collection field12;
        TestBean2 field13;
        Date field14;

        public Map getField11() {
            return field11;
        }

        public void setField11(Map field11) {
            this.field11 = field11;
        }

        public Collection getField12() {
            return field12;
        }

        public void setField12(Collection field12) {
            this.field12 = field12;
        }

        public TestBean2 getField13() {
            return field13;
        }

        public void setField13(TestBean2 field13) {
            this.field13 = field13;
        }

        public Date getField14() {
            return field14;
        }

        public void setField14(Date field14) {
            this.field14 = field14;
        }
    }

    private static class TestBean2 {
        Map field21;
        Collection field22;

        public Map getField21() {
            return field21;
        }

        public void setField21(Map field21) {
            this.field21 = field21;
        }

        public Collection getField22() {
            return field22;
        }

        public void setField22(Collection field22) {
            this.field22 = field22;
        }
    }

    @Test
    public void testFromClientRequest() throws Exception {
        ClientRequest request = new ClientRequest("uriTemplate");
        request.setHttpMethod("GET");
        request.overrideUri(URI.create("http://test.com/tester?test=true"));
        request.formParameter("formParameter1", "formValue1")
                .pathParameter("pathParameter1", "pathValue1")
                .header("header1", "headerValue1")
                .body(MediaType.APPLICATION_JSON, "{\"jsonData\":[\"jsonValue1\",\"jsonValue2\"]}");

        final String toStringString = ToString.clientRequestToString(request);
        assertEquals("GET " + request.getUri() + LINE_SEPARATOR +
                "Parameters: {formParameter1=[formValue1]}" + LINE_SEPARATOR +
                "Parameters: {pathParameter1=[pathValue1]}" + LINE_SEPARATOR +
                "Headers: {header1=[headerValue1]}" + LINE_SEPARATOR +
                "Entity: [value={{,\",j,s,o,n,D,a,t,a,\",:,[,\",j,s,o,n,V,a,l,u,e,1,\",,,\",j,s,o,n,V,a,l,u,e,2,\",],}}, " + LINE_SEPARATOR + "  hash=0]",
                toStringString);
    }

    @Test
    public void testFromClientResponse() throws Exception {
        final BaseClientResponse response = mock(BaseClientResponse.class);
        final BaseClientResponseStreamFactory factory = mock(BaseClientResponseStreamFactory.class);
        final MultivaluedMap<String, String> multivaluedMap = new MultivaluedMapImpl<>();
        multivaluedMap.putSingle("key", "value");
        given(response.getResponseStatus()).willReturn(Status.fromStatusCode(200));
        given(response.getStatus()).willReturn(200);
        given(response.getHeaders()).willReturn(multivaluedMap);
        given(factory.getInputStream()).willReturn(IOUtils.toInputStream("HELLO WORLD!!", "UTF-8"));
        given(response.getStreamFactory()).willReturn(factory);
        final String toStringString = ToString.clientResponseToString(response);
        assertEquals("Response-Code: 200 OK" + LINE_SEPARATOR + "Headers: {key=[value]}" + LINE_SEPARATOR + "Payload: HELLO WORLD!!", toStringString);
    }

    @Test
    public void testFromHttpRequest() throws Exception {
        final HttpRequest request = mock(HttpRequest.class);
        final UriInfo uriinfo = mock(UriInfo.class);
        final MultivaluedMap<String, String> multivaluedMap = new MultivaluedMapImpl<>();
        multivaluedMap.putSingle("who", "me & bobby McGee");

        final MultivaluedMap<String, String> multivaluedMap2 = new MultivaluedMapImpl<>();
        multivaluedMap2.putSingle("requestheader", "value");
        final HttpHeaders headers = new HttpHeadersImpl() {
            {
                this.setRequestHeaders(multivaluedMap2);
            }
        };

        given(uriinfo.getRequestUri()).willReturn(URI.create("http://pretty.silly.me/a/uri?maybe=true"));
        given(request.getUri()).willReturn(uriinfo);
        given(request.getHttpMethod()).willReturn("GET");
        given(request.getDecodedFormParameters()).willReturn(multivaluedMap);
        given(request.getHttpHeaders()).willReturn(headers);
        final String toStringString = ToString.httpRequestToString(request);
        assertEquals("GET http://pretty.silly.me/a/uri?maybe=true" + LINE_SEPARATOR + "Parameters: {who=[me & bobby McGee]}" + LINE_SEPARATOR +
                "Headers: {requestheader=[value]}", toStringString);
    }
}
