package com.axiell.ehub.provider.ep;

import com.axiell.ehub.provider.record.format.FormatBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class FormatDTOTest {
    private FormatDTO underTest = new FormatDTO(FormatBuilder.downloadableFormat().id());

    @Test
    public void serialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(underTest);
        System.out.println(json);
        FormatDTO FormatDTO = objectMapper.readValue(json, FormatDTO.class);
        Assert.assertThat(FormatDTO.getId(), Matchers.is(underTest.getId()));
    }
}
