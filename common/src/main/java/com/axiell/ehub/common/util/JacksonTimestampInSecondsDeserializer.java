package com.axiell.ehub.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;


public class JacksonTimestampInSecondsDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(final JsonParser jp, final DeserializationContext deserializationContext) throws IOException {
        String value = jp.getText().trim();
        return new Date(Long.parseLong(value) * 1000);
    }
}
