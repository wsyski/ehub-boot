package com.axiell.ehub.common.controller.provider.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Consumes(MediaType.WILDCARD) // NOTE: required to support "non-standard" JSON variants
@Produces({MediaType.APPLICATION_JSON, "text/json", MediaType.WILDCARD})
public class JsonProvider extends JacksonJsonProvider {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    public JsonProvider() {
        super();
        setMapper(OBJECT_MAPPER);
    }
}
