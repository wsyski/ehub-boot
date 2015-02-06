package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutDTOTest.class);
    private ObjectMapper mapper;
    private CheckoutMetadataDTO expCheckoutMetadataDTO;
    private ContentLinkDTO expContentLinkDTO;
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
        expCheckoutMetadataDTO = new CheckoutMetadataDTO().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2").format(formatDTO);
        expContentLinkDTO = new ContentLinkDTO().href("href1");
        expCheckoutDTO = new CheckoutDTO().metadata(expCheckoutMetadataDTO).contentLink(expContentLinkDTO);
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
        actCheckoutDTO = mapper.readValue(expJson, CheckoutDTO.class);
    }

    private void thenActualCheckoutDTOEqualsExpectedCheckoutDTO() {
        CheckoutMetadataDTO actCheckoutMetadataDTO = actCheckoutDTO.getMetadata();
        assertThat(actCheckoutMetadataDTO, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO));
        ContentLinkDTO actContentLinkDTO = actCheckoutDTO.getContentLink();
        assertEquals(expContentLinkDTO.getHref(), actContentLinkDTO.getHref());
    }
}
