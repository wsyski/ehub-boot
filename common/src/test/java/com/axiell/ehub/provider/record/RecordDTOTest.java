package com.axiell.ehub.provider.record;

import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

public class RecordDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordDTOTest.class);
    private ObjectMapper mapper;
    private FormatDTO expFormatDTO;
    private RecordDTO expRecordDTO;
    private String expJson;
    private RecordDTO actRecordDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        expFormatDTO = new FormatDTO().id("formatId").description("description").name("name").contentDisposition(ContentDisposition.DOWNLOADABLE);
        expRecordDTO = new RecordDTO().id("recordId").formats(Lists.newArrayList(expFormatDTO));
    }

    @Test
    public void unmarshalRecordDTO() throws IOException {
        givenExpectedRecordDTOAsJson();
        whenUnmarshalRecordDTOJson();
        thenActualRecordDTOEqualsExpected();
    }

    private void givenExpectedRecordDTOAsJson() throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, expRecordDTO);
        expJson = writer.toString();
        LOGGER.debug(expJson);
    }

    private void whenUnmarshalRecordDTOJson() throws IOException {
        actRecordDTO = mapper.readValue(expJson, RecordDTO.class);
    }

    private void thenActualRecordDTOEqualsExpected() {
        assertEquals(expRecordDTO.getId(), actRecordDTO.getId());
        FormatDTO actFormatDTO = actRecordDTO.getFormats().get(0);
        assertEquals(expFormatDTO.getId(), actFormatDTO.getId());
        assertEquals(expFormatDTO.getDescription(), actFormatDTO.getDescription());
        assertEquals(expFormatDTO.getName(), actFormatDTO.getName());
        assertEquals(expFormatDTO.getContentDisposition(), actFormatDTO.getContentDisposition());
    }
}
