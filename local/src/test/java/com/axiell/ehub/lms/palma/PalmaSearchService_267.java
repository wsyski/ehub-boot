package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.search.v267.service.Catalogue;
import com.axiell.arena.services.palma.search.v267.service.Search.SearchRequest;
import com.axiell.arena.services.palma.search.v267.service.SearchResponse;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.provider.ContentProviderName;
import junit.framework.Assert;

import javax.jws.WebService;

@WebService(
        serviceName = "PalmaSearchService",
        portName = "catalogue",
        targetNamespace = "http://service.v267.search.palma.services.arena.axiell.com/",
        wsdlLocation = "com/axiell/ehub/lms/palma_267/catalogue.wsdl",
        endpointInterface = "com.axiell.arena.services.palma.search.v267.service.Catalogue")

public class PalmaSearchService_267 extends AbstractPalmaService implements Catalogue {
    private static final String CONTEXT_PATH = "com.axiell.arena.services.palma.search.v267.service";


    @Override
    public com.axiell.arena.services.palma.search.v267.service.GetHoldingsResponse.GetHoldingResult getHoldings(
            com.axiell.arena.services.palma.search.v267.service.GetHoldings.GetHoldingsRequest getHoldingsRequest) {
        return null;
    }

    @Override
    public com.axiell.arena.services.palma.search.v267.service.SearchResponse.SearchResult search(final SearchRequest searchRequest) {
        verifySearchRequest(searchRequest);
        return ((SearchResponse)getFileResponseUnmarshaller().unmarshalFromFile(PALMA_SEARCH_RESPONSE_XML)).getSearchResult();

    }

    @Override
    public com.axiell.arena.services.palma.search.v267.service.GetCatalogueRecordDetailResponse.CatalogueRecordDetailResult getCatalogueRecordDetail(
            com.axiell.arena.services.palma.search.v267.service.GetCatalogueRecordDetail.CatalogueRecordDetailRequest catalogueRecordDetailRequest) {
        return null;
    }

    protected void verifySearchRequest(final SearchRequest searchRequest) {
        Assert.assertNotNull(searchRequest);
        Assert.assertEquals(searchRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(searchRequest.getQuery(), "contentProviderRecordId_index: \""+DevelopmentData.ELIB_RECORD_0_ID+"\" AND contentProviderName_index: \""+
                ContentProviderName.ELIB+"\"");
        Assert.assertEquals(searchRequest.getAvailability().getEnable(), "no");
        Assert.assertEquals(searchRequest.getFacets().getEnable(), "no");
        Assert.assertEquals(searchRequest.getPage().intValue(), 1);
        Assert.assertEquals(searchRequest.getPageSize().intValue(), 1);
    }

    @Override
    protected String getContextPath() {
        return CONTEXT_PATH;
    }
}