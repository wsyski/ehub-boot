package com.axiell.ehub.common.search;


import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.CheckoutMetadataDTO;
import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.SearchResultDTO;
import com.axiell.ehub.common.provider.record.format.ContentDisposition;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.FormatDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.common.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class SearchResultDTOTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private CheckoutMetadataDTO expCheckoutMetadataDTO;
    private FormatDTO expFormatDTO;
    private SearchResultDTO<CheckoutMetadataDTO> searchResultDTO;
    private String expJson;
    private SearchResultDTO<CheckoutMetadataDTO> actSearchResultDTO;


    @BeforeEach
    public void setUpExpectedDTO() {
        expFormatDTO = new FormatDTO("id", "name", ContentDisposition.DOWNLOADABLE).description("description");
        expCheckoutMetadataDTO = new CheckoutMetadataDTO(2L, "lmsLoanId", new Date(), true, expFormatDTO).contentProviderLoanId("contentProviderLoan2");
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
        OBJECT_MAPPER.writeValue(writer, searchResultDTO);
        expJson = writer.toString();
        log.debug(expJson);
    }

    private void whenUnmarshalSearchResultDTOJson() throws IOException {
        actSearchResultDTO = OBJECT_MAPPER.readValue(expJson, new TypeReference<SearchResultDTO<CheckoutMetadataDTO>>() {
        });
    }

    private void thenActualSearchResultDTOEqualsExpected() {
        Assertions.assertEquals(searchResultDTO.getOffset(), actSearchResultDTO.getOffset());
        Assertions.assertEquals(searchResultDTO.getLimit(), actSearchResultDTO.getLimit());
        Assertions.assertEquals(searchResultDTO.getTotalItems(), actSearchResultDTO.getTotalItems());
        List<CheckoutMetadataDTO> actItems = actSearchResultDTO.getItems();
        Assertions.assertEquals(1, actItems.size());
        CheckoutMetadataDTO actCheckoutMetadataDTO = actItems.get(0);
        assertThat(actCheckoutMetadataDTO, matchesExpectedCheckoutMetadataDTO(expCheckoutMetadataDTO));
    }
}
