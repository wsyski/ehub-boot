package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CheckoutDTOOverdriveTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String readFile(final String fileName) throws IOException {
        return IOUtils.toString(new ClassPathResource("com/axiell/ehub/provider/overdrive/" + fileName).getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    public void deserialize() throws IOException {
        String json = readFile("checkout.json");
        CheckoutDTO actualObject = whenDeserializeObject(json);
        assertThat("Invalid reserveId", actualObject.getReserveId(), is("e7844604-f059-4a71-954a-1d5baf4dc53e"));
        assertThat("Invalid isFormatLocked", actualObject.isFormatLocked(), is(true));
    }

    protected CheckoutDTO whenDeserializeObject(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, CheckoutDTO.class);
    }
}
