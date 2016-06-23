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

import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutMetadataDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutMetadataDTOTest.class);
    private ObjectMapper mapper;
    private CheckoutMetadataDTO expCheckoutMetadataDTO1;
    private CheckoutMetadataDTO expCheckoutMetadataDTO2;
    private FormatDTO expFormatDTO;
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
        expFormatDTO = new FormatDTO("id","name",ContentDisposition.DOWNLOADABLE).description("description");
        expCheckoutMetadataDTO2 = new CheckoutMetadataDTO().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2").format(expFormatDTO);
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
        assertThat(actCheckoutMetadataDTO2, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO2));
    }
}
