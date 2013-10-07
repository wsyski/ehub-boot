package com.axiell.ehub.provider.publit;


import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ProductTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String json;
    private Product result;


    @Test
    public void parseValidJsonWithConfiguredParameters() throws IOException {
        givenJsonContainsAllFieldData();
        whenJsonIsRead();
        thenProductContainsAllExpectedData();
    }

    @Test
    public void parseValidJsonWithExtraParameter() throws IOException {
        givenJsonContainsAllFieldDataWithAnExtraField();
        whenJsonIsRead();
        thenProductContainsAllExpectedData();
    }

    private void thenProductContainsAllExpectedData() {
        assertEquals(1234, result.getId().intValue());
        assertEquals("a type", result.getType());
        assertEquals("a title", result.getTitle());
        assertEquals("a subtitle", result.getSubTitle());
        assertEquals("a publisher", result.getPublisher());
        assertEquals("2013", result.getOriginalPublicationYear());
    }

    private void whenJsonIsRead() throws IOException {
        result = MAPPER.readValue(json, Product.class);
    }

    private void givenJsonContainsAllFieldData() {
        json = "{\n" +
                "    \"id\": 1234,\n" +
                "    \"type\": \"a type\",\n" +
                "    \"title\": \"a title\",\n" +
                "    \"subTitle\": \"a subtitle\",\n" +
                "    \"publisher\": \"a publisher\",\n" +
                "    \"original_publication_year\": \"2013\"\n" +
                "}";
    }

    private void givenJsonContainsAllFieldDataWithAnExtraField() {
        json = "{\n" +
                "    \"id\": 1234,\n" +
                "    \"type\": \"a type\",\n" +
                "    \"title\": \"a title\",\n" +
                "    \"subTitle\": \"a subtitle\",\n" +
                "    \"publisher\": \"a publisher\",\n" +
                "    \"original_publication_year\": \"2013\",\n" +
                "    \"extra_parameter\": \"bogus\"\n" +
                "}";
    }
}
