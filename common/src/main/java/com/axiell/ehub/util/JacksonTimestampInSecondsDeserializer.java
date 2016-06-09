package com.axiell.ehub.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;


public class JacksonTimestampInSecondsDeserializer extends JsonDeserializer<Date> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonTimestampInSecondsDeserializer.class);

    @Override
    public Date deserialize(final JsonParser jp, final DeserializationContext deserializationContext) throws IOException {
        String value = jp.getText().trim();

        try {
            return new Date(Long.valueOf(value) * 1000);
        } catch (NumberFormatException ex) {
            LOGGER.error("Unable to deserialize timestamp: " + value, ex);
            return null;
        }
    }
}
