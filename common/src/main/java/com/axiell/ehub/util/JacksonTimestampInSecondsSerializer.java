package com.axiell.ehub.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;


public class JacksonTimestampInSecondsSerializer extends JsonSerializer<Date> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonTimestampInSecondsSerializer.class);

    @Override
    public void serialize (final Date value, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.valueOf(value.getTime()/1000));
    }
}
