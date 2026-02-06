package com.axiell.ehub.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;


public class JacksonTimestampInSecondsSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(final Date value, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.valueOf(value.getTime() / 1000));
    }
}
