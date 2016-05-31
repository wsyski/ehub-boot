package com.axiell.ehub.provider.ep.lpf;

import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CheckoutDTOTest {
    private CheckoutDTO underTest=CheckoutDTOBuilder.defaultCheckoutDTO();

    @Test
    public void serialize() throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        String json = objectMapper.writeValueAsString(underTest);
        System.out.println(json);
        CheckoutDTO checkoutDTO=objectMapper.reader().withType(CheckoutDTO.class).readValue(json);
        Assert.assertThat(checkoutDTO.getId(), Matchers.is(underTest.getId()));
    }
}
