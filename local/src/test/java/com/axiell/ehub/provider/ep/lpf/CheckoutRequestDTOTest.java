package com.axiell.ehub.provider.ep.lpf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CheckoutRequestDTOTest {
    private CheckoutRequestDTO underTest = new CheckoutRequestDTO("requestId","formatId");

    @Test
    public void serialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(underTest);
        System.out.println(json);
        CheckoutRequestDTO checkoutRequestDTO = objectMapper.readValue(json, CheckoutRequestDTO.class);
        Assert.assertThat(checkoutRequestDTO.getRecordId(), Matchers.is(underTest.getRecordId()));
        Assert.assertThat(checkoutRequestDTO.getFormatId(), Matchers.is(underTest.getFormatId()));
    }
}
