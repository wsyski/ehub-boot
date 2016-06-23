package com.axiell.ehub.search;


import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchResultDTOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultDTOTest.class);
    private CheckoutMetadataDTO expCheckoutMetadataDTO;
    private FormatDTO expFormatDTO;
    private ObjectMapper mapper;
    private SearchResultDTO<CheckoutMetadataDTO> searchResultDTO;
    private String expJson;
    private SearchResultDTO<CheckoutMetadataDTO> actSearchResultDTO;

    @Before
    public void setUpObjectMapper() {
        mapper = new ObjectMapper();
    }

    @Before
    public void setUpExpectedDTO() {
        expFormatDTO = new FormatDTO("id", "name", ContentDisposition.DOWNLOADABLE).description("description");
        expCheckoutMetadataDTO =
                new CheckoutMetadataDTO().id(2L).contentProviderLoanId("contentProviderLoan2").expirationDate(new Date()).lmsLoanId("lmsLoanId2")
                        .format(expFormatDTO);
        List<CheckoutMetadataDTO> items = Lists.newArrayList(expCheckoutMetadataDTO);
        searchResultDTO = new SearchResultDTO<>();
        searchResultDTO.items(items).offset(1).limit(2).totalItems(3);
    }

    @Test
    public void unmarshalSearchResultDTO() throws IOException {
        givenExpectedSearchResultDTOAsJson();
        whenUnmarshalSearchResultDTOJson();
        thenActualSearchResultDTOEqualsExpected();
    }

    private void givenExpectedSearchResultDTOAsJson() throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, searchResultDTO);
        expJson = writer.toString();
        LOGGER.debug(expJson);
    }

    private void whenUnmarshalSearchResultDTOJson() throws IOException {
        actSearchResultDTO = mapper.readValue(expJson, new TypeReference<SearchResultDTO<CheckoutMetadataDTO>>() {
        });
    }

    private void thenActualSearchResultDTOEqualsExpected() {
        assertEquals(searchResultDTO.getOffset(), actSearchResultDTO.getOffset());
        assertEquals(searchResultDTO.getLimit(), actSearchResultDTO.getLimit());
        assertEquals(searchResultDTO.getTotalItems(), actSearchResultDTO.getTotalItems());
        List<CheckoutMetadataDTO> actItems = actSearchResultDTO.getItems();
        assertEquals(1, actItems.size());
        CheckoutMetadataDTO actCheckoutMetadataDTO = actItems.get(0);
        assertThat(actCheckoutMetadataDTO, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO));
    }
}
