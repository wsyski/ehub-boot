package com.axiell.ehub.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;


public class JacksonTimestampDeserializer extends JsonDeserializer<Date> {
    Logger logger = LoggerFactory.getLogger(JacksonTimestampDeserializer.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String timestamp = jp.getText().trim();

        try {
            return new Date(Long.valueOf(timestamp + "000"));
        } catch (NumberFormatException ex) {
            logger.error("Unable to deserialize timestamp: " + timestamp, ex);
            return null;
        }
    }
}
