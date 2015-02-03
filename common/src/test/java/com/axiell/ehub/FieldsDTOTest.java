package com.axiell.ehub;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class FieldsDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldsDTOTest.class);
    private ObjectMapper mapper;
    private FieldsDTO expDTO;
    private String expJson;
    private FieldsDTO actDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        expDTO = new FieldsDTO();
        expDTO.getFields().put("reserveId", "76C1B7D0-17F4-4C05-8397-C66C17411584");
        expDTO.getFields().put("recordId", "123456789");
    }

    @Test
    public void unmarshalFieldsDTO() throws IOException {
        givenExpectedFieldsDTOAsJson();
        whenUnmarshalFieldsDTOJson();
        thenActualFieldsDTOEqualsExpectedFieldsDTO();
    }

    private void givenExpectedFieldsDTOAsJson() throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, expDTO);
        expJson = writer.toString();
        LOGGER.debug(expJson);
    }

    private void whenUnmarshalFieldsDTOJson() throws IOException {
        StringReader reader = new StringReader(expJson);
        actDTO = mapper.readValue(reader, FieldsDTO.class);
    }

    private void thenActualFieldsDTOEqualsExpectedFieldsDTO() {
        Map<String, String> actFields = actDTO.getFields();
        for (Map.Entry<String, String> entry : expDTO.getFields().entrySet()) {
            String expValue = entry.getValue();
            String actValue = actFields.get(entry.getKey());
            Assert.assertEquals(expValue, actValue);
        }
    }
}
