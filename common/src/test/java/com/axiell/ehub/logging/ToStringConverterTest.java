/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.logging;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse.BaseClientResponseStreamFactory;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito.BDDMyOngoingStubbing;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ToStringConverterTest {

    @Mock
    private ServerResponse serverResponse;

    @Mock
    private MultivaluedMap<String, Object> metaData;

    @Mock
    private Set<Entry<String, List<Object>>> metaDataEntries;

    @Mock
    private Iterator<Entry<String, List<Object>>> metaDataEntriesIterator;

    @Mock
    private SOAPMessage soapMessage;

    private String actualResult;

    @Test
    public void testToString() throws UnsupportedEncodingException {
        Map<String, String> map1 = new LinkedHashMap<>();
        map1.put("oneKey", "oneValue");
        map1.put("twoKey", "twoValue");
        map1.put("threeKey", "threeValue");
        String toString = ToStringConverter.mapToString(map1);
        assertEquals("{oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}", toString);
        Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
        map2.put("oneMap", map1);
        map2.put("twoMap", map1);
        toString = ToStringConverter.mapToString(map2);
        assertEquals("{oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}",
                toString);
        Collection<String> collection = Arrays.asList("col1", "col2", "col3");
        toString = ToStringConverter.collectionToString(collection);
        assertEquals("[col1, col2, col3]", toString);
        TestBean2 testBean2 = new TestBean2();
        testBean2.setField21(map2);
        testBean2.setField22(collection);
        TestBean1 testBean1 = new TestBean1();
        testBean1.setField11(map1);
        testBean1.setField12(collection);
        testBean1.setField13(testBean2);
        testBean1.setField14(new Date(0L));
        toString = ToStringConverter.objectToString(testBean1);
        assertEquals("[field11={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, " + SystemUtils.LINE_SEPARATOR +
                "  field12=[col1, col2, col3], " + SystemUtils.LINE_SEPARATOR +
                "  field13=[field21={oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}, " +
                SystemUtils.LINE_SEPARATOR +
                "  field22=[col1, col2, col3]], " + SystemUtils.LINE_SEPARATOR +
                "  field14=1970-01-01 01:00:00]", toString);
        Map<String, TestBean2> map3 = Collections.singletonMap("oneBean", testBean2);
        toString = ToStringConverter.mapToString(map3);
        assertEquals(
                "{oneBean=[field21={oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}, " +
                        SystemUtils.LINE_SEPARATOR +
                        "  field22=[col1, col2, col3]]}", toString);
    }

    private static class TestBean1 {
        Map<?, ?> field11;
        Collection<?> field12;
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

        final String toStringString = ToStringConverter.clientRequestToString(request);
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
        final MultivaluedMap<String, Object> multivaluedMap = new MultivaluedMapImpl<>();
        multivaluedMap.putSingle("key", "value");
        given(response.getResponseStatus()).willReturn(Status.fromStatusCode(200));
        given(response.getStatus()).willReturn(200);
        given(response.getHeaders()).willReturn(multivaluedMap);
        given(factory.getInputStream()).willReturn(IOUtils.toInputStream("HELLO WORLD!!", "UTF-8"));
        given(response.getStreamFactory()).willReturn(factory);
        final String toStringString = ToStringConverter.clientResponseToString(response);
        assertEquals("Response-Code: 200 OK" + LINE_SEPARATOR + "Headers: {key=[value]}" + LINE_SEPARATOR + "Payload: HELLO WORLD!!", toStringString);
    }

    @Test
    public void testFromHttpRequest() throws Exception {
        final HttpRequest request = mock(HttpRequest.class);
        final ResteasyUriInfo uriinfo = mock(ResteasyUriInfo.class);
        final MultivaluedMap<String, String> multivaluedMap = new MultivaluedMapImpl<>();
        multivaluedMap.putSingle("who", "me & bobby McGee");

        final MultivaluedMap<String, String> multivaluedMap2 = new MultivaluedMapImpl<>();
        multivaluedMap2.putSingle("requestheader", "value");
        final HttpHeaders headers = new ResteasyHttpHeaders(multivaluedMap2);

        given(uriinfo.getRequestUri()).willReturn(URI.create("http://pretty.silly.me/a/uri?maybe=true"));
        given(request.getUri()).willReturn(uriinfo);
        given(request.getHttpMethod()).willReturn("GET");
        given(request.getDecodedFormParameters()).willReturn(multivaluedMap);
        given(request.getHttpHeaders()).willReturn(headers);
        final String toStringString = ToStringConverter.httpRequestToString(request);
        assertEquals("GET http://pretty.silly.me/a/uri?maybe=true" + LINE_SEPARATOR + "Parameters: {who=[me & bobby McGee]}" + LINE_SEPARATOR +
                "Headers: {requestheader=[value]}", toStringString);
    }

    @Test
    public void serverResponseToString() {
        givenServerResponseHasAResponseStatus();
        givenServerResponseHasNonEmptyMetadata();
        givenServerResponseHasAnEntity();
        whenServerResponseIsTransformedToString();
        thenResultingStringHasAppropriateData();
    }

    @Test
    public void soapMessageToString() throws IOException, SOAPException {
        whenSoapMessageIsProcessed();
        thenSoapMessageIsWrittenToOutputStream();
    }

    private void thenSoapMessageIsWrittenToOutputStream() throws SOAPException, IOException {
        verify(soapMessage).writeTo(any(StringBuilderOutputStream.class));
    }

    private String whenSoapMessageIsProcessed() {
        return ToStringConverter.soapMessageToString(soapMessage);
    }

    private void thenResultingStringHasAppropriateData() {
        assertEquals("Response-Code: 200" + System.getProperty("line.separator") + "Metadata: {first=[second, third], fourth=[fifth]}" + System.getProperty("line.separator") + "Entity: [aProperty=one, " + System.getProperty("line.separator") + "  anotherProperty=two]", actualResult);
    }

    private void whenServerResponseIsTransformedToString() {
        actualResult = ToStringConverter.serverResponseToString(serverResponse);
    }

    private void givenServerResponseHasAnEntity() {
        given(serverResponse.getEntity()).willReturn(new TestPojo("one", "two"));
    }

    private void givenServerResponseHasNonEmptyMetadata() {
        given(serverResponse.getMetadata()).willReturn(metaData);
        given(metaData.isEmpty()).willReturn(false);
        given(metaData.entrySet()).willReturn(metaDataEntries);
        given(metaDataEntries.iterator()).willReturn(metaDataEntriesIterator);
        given(metaDataEntriesIterator.hasNext())
                .willReturn(true)
                .willReturn(false);
        given(metaDataEntriesIterator.next())
                .willReturn(new SimpleEntry<String, List<Object>>("first", Lists.<Object>newArrayList("second", "third")))
                .willReturn(new SimpleEntry<String, List<Object>>("fourth", Lists.<Object>newArrayList("fifth")));
    }

    private BDDMyOngoingStubbing<Integer> givenServerResponseHasAResponseStatus() {
        return given(serverResponse.getStatus()).willReturn(200);
    }
}
