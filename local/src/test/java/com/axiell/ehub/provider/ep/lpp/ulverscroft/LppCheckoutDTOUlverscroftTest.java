package com.axiell.ehub.provider.ep.lpp.ulverscroft;

import com.axiell.ehub.provider.ep.lpp.LppCheckoutDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LppCheckoutDTOUlverscroftTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void deserialize() throws IOException {
        String json = readFile("checkout.json");
        LppCheckoutDTO actualObject = whenDeserializeObject(json);
    }

    protected LppCheckoutDTO whenDeserializeObject(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, LppCheckoutDTO.class);
    }

    private static String readFile(final String fileName) throws IOException {
        return IOUtils.toString(new ClassPathResource("com/axiell/ehub/provider/ulverscroft/" + fileName).getInputStream(), StandardCharsets.UTF_8);
    }
}
