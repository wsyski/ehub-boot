package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutDTOTest {
    private ObjectMapper mapper;
    private CheckoutMetadataDTO expCheckoutMetadataDTO;
    private CheckoutDTO expCheckoutDTO;
    private String expJson;
    private CheckoutDTO actCheckoutDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        FormatDTO formatDTO = new FormatDTO().id("id").description("description").name("name").contentDisposition(ContentDisposition.DOWNLOADABLE);
        expCheckoutMetadataDTO =
                new CheckoutMetadataDTO().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2")
                        .format(formatDTO);
        expCheckoutDTO = new CheckoutDTO().metadata(expCheckoutMetadataDTO).contentLinks(ContentLinkBuilder.defaultContentLinks().toDTO()).supplementLinks(SupplementLinkBuilder.defaultSupplementLinks().toDTO());
    }

    @Test
    public void unmarshalCheckoutDTO() throws IOException {
        givenExpectedCheckoutDTOAsJson();
        whenUnmarshalCheckoutDTOJson();
        thenActualCheckoutDTOEqualsExpectedCheckoutDTO();
    }

    private void givenExpectedCheckoutDTOAsJson() throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, expCheckoutDTO);
        expJson = writer.toString();
        System.out.println(expJson);
    }

    private void whenUnmarshalCheckoutDTOJson() throws IOException {
        actCheckoutDTO = mapper.readValue(expJson, CheckoutDTO.class);
    }

    private void thenActualCheckoutDTOEqualsExpectedCheckoutDTO() {
        CheckoutMetadataDTO actCheckoutMetadataDTO = actCheckoutDTO.getMetadata();
        assertThat(actCheckoutMetadataDTO, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO));
        List<ContentLinkDTO> actContentLinksDTO = actCheckoutDTO.getContentLinks();
        assertEquals(1, actContentLinksDTO.size());
        assertEquals(ContentLinkBuilder.defaultContentLinks().getContentLinks().get(0).href(), actContentLinksDTO.get(0).getHref());
        List<SupplementLinkDTO> actSupplementLinksDTO = actCheckoutDTO.getSupplementLinks();
        assertEquals(1, actSupplementLinksDTO.size());
        assertEquals(SupplementLinkBuilder.defaultSupplementLinks().getSupplementLinks().get(0).href(), actSupplementLinksDTO.get(0).getHref());
        assertEquals(SupplementLinkBuilder.defaultSupplementLinks().getSupplementLinks().get(0).name(), actSupplementLinksDTO.get(0).getName());
    }
}
