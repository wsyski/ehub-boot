package com.axiell.ehub.common;

import com.axiell.ehub.common.FieldsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class FieldsDTOTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private FieldsDTO expDTO;
    private String expJson;
    private FieldsDTO actDTO;

    @BeforeEach
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
        OBJECT_MAPPER.writeValue(writer, expDTO);
        expJson = writer.toString();
        log.debug(expJson);
    }

    private void whenUnmarshalFieldsDTOJson() throws IOException {
        StringReader reader = new StringReader(expJson);
        actDTO = OBJECT_MAPPER.readValue(reader, FieldsDTO.class);
    }

    private void thenActualFieldsDTOEqualsExpectedFieldsDTO() {
        Map<String, String> actFields = actDTO.getFields();
        for (Map.Entry<String, String> entry : expDTO.getFields().entrySet()) {
            String expValue = entry.getValue();
            String actValue = actFields.get(entry.getKey());
            Assertions.assertEquals(expValue, actValue);
        }
    }
}
