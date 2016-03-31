package com.axiell.ehub.v2.checkout;

import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.v2.provider.record.format.FormatDTO_v2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import static com.axiell.ehub.v2.checkout.CheckoutMetadataDTOMatcher_v2.matchesExpectedCheckoutMetadataDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutDTOV2Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutDTOV2Test.class);
    private ObjectMapper mapper;
    private CheckoutMetadataDTO_v2 expCheckoutMetadataDTO;
    private ContentLinkDTO expContentLinkDTO;
    private CheckoutDTO_v2 expCheckoutDTO;
    private String expJson;
    private CheckoutDTO_v2 actCheckoutDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        FormatDTO_v2 formatDTO = new FormatDTO_v2().id("id").description("description").name("name").contentDisposition(ContentDisposition.DOWNLOADABLE);
        expCheckoutMetadataDTO =
                new CheckoutMetadataDTO_v2().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2")
                        .format(formatDTO);
        expContentLinkDTO = new ContentLinkDTO().href("href1");
        expCheckoutDTO = new CheckoutDTO_v2().metadata(expCheckoutMetadataDTO).contentLink(expContentLinkDTO);
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
        LOGGER.debug(expJson);
    }

    private void whenUnmarshalCheckoutDTOJson() throws IOException {
        actCheckoutDTO = mapper.readValue(expJson, CheckoutDTO_v2.class);
    }

    private void thenActualCheckoutDTOEqualsExpectedCheckoutDTO() {
        CheckoutMetadataDTO_v2 actCheckoutMetadataDTO = actCheckoutDTO.getMetadata();
        assertThat(actCheckoutMetadataDTO, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO));
        ContentLinkDTO actContentLinkDTO = actCheckoutDTO.getContentLink();
        assertEquals(expContentLinkDTO.getHref(), actContentLinkDTO.getHref());
    }
}
