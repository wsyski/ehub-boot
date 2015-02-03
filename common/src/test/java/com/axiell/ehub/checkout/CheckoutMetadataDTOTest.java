package com.axiell.ehub.checkout;

import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CheckoutMetadataDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutMetadataDTOTest.class);
    private ObjectMapper mapper;
    private CheckoutMetadataDTO expCheckoutMetadataDTO1;
    private CheckoutMetadataDTO expCheckoutMetadataDTO2;
    private List<CheckoutMetadataDTO> expCheckoutsDTO;
    private String expJson;
    private List<CheckoutMetadataDTO> actDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        expCheckoutMetadataDTO1 = new CheckoutMetadataDTO().id(1L);

        FormatDTO formatDTO = new FormatDTO().id("id").description("description").name("name").contentDisposition(ContentDisposition.DOWNLOADABLE);
        expCheckoutMetadataDTO2 = new CheckoutMetadataDTO().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2").format(formatDTO);
        expCheckoutsDTO = new ArrayList<>();
        expCheckoutsDTO.add(expCheckoutMetadataDTO1);
        expCheckoutsDTO.add(expCheckoutMetadataDTO2);
    }

    @Test
    public void unmarshalCheckoutsDTO() throws IOException {
        givenExpectedCheckoutDTOAsJson();
        whenUnmarshalCheckoutsDTOJson();
        thenActualCheckoutsDTOEqualsExpectedCheckoutsDTO();
    }

    private void givenExpectedCheckoutDTOAsJson() throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, expCheckoutsDTO);
        expJson = writer.toString();
        LOGGER.debug(expJson);
    }

    private void whenUnmarshalCheckoutsDTOJson() throws IOException {
        StringReader reader = new StringReader(expJson);
        actDTO = mapper.readValue(reader, new TypeReference<List<CheckoutMetadataDTO>>() {
        });
    }

    private void thenActualCheckoutsDTOEqualsExpectedCheckoutsDTO() {
        assertEquals(2, actDTO.size());
        CheckoutMetadataDTO actCheckoutMetadataDTO1 = actDTO.get(0);
        assertEquals(expCheckoutMetadataDTO1.getId(), actCheckoutMetadataDTO1.getId());
        CheckoutMetadataDTO actCheckoutMetadataDTO2 = actDTO.get(1);
        assertEquals(expCheckoutMetadataDTO2.getId(), actCheckoutMetadataDTO2.getId());
        assertEquals(expCheckoutMetadataDTO2.getLmsLoanId(), actCheckoutMetadataDTO2.getLmsLoanId());
        assertEquals(expCheckoutMetadataDTO2.getContentProviderLoanId(), actCheckoutMetadataDTO2.getContentProviderLoanId());
        assertEquals(expCheckoutMetadataDTO2.getExpirationDate(), actCheckoutMetadataDTO2.getExpirationDate());
    }
}
