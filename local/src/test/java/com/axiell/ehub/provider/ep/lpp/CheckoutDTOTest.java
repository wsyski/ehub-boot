package com.axiell.ehub.provider.ep.lpp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CheckoutDTOTest {
    private CheckoutDTO underTest = CheckoutDTOBuilder.defaultCheckoutDTO();

    @Test
    public void serialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(underTest);
        System.out.println(json);
        CheckoutDTO checkoutDTO = objectMapper.readValue(json, CheckoutDTO.class);
        Assert.assertThat(checkoutDTO.getId(), Matchers.is(underTest.getId()));
    }
}
