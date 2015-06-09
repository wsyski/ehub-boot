package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.search.v267.service.Catalogue;
import com.axiell.arena.services.palma.search.v267.service.Search;
import com.axiell.arena.services.palma.search.v267.service.SearchResponse;
import com.axiell.arena.services.palma.v267.util.LanguageType;
import com.axiell.ehub.consumer.EhubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER;

@Component
class CatalogueFacade implements ICatalogueFacade {
    private static final com.axiell.arena.services.palma.search.v267.service.ObjectFactory SEARCH_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.search.v267.service.ObjectFactory();

    @Autowired
    private ICataloguePortFactory cataloguePortFactory;

    @Override
    public SearchResponse.SearchResult search(final EhubConsumer ehubConsumer, String contentProviderName, final String contentProviderRecordId) {
        Search.SearchRequest searchRequest = createSearchRequest(ehubConsumer, contentProviderName, contentProviderRecordId);
        Catalogue catalogue = cataloguePortFactory.getInstance(ehubConsumer);
        return catalogue.search(searchRequest);
    }

    private static Search.SearchRequest createSearchRequest(final EhubConsumer ehubConsumer, String contentProviderName, final String contentProviderRecordId) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        Search.SearchRequest searchRequest = SEARCH_OBJECT_FACTORY.createSearchSearchRequest();
        searchRequest.setArenaMember(agencyMemberIdentifier);
        searchRequest.setQuery("contentProviderRecordId_index: \"" + contentProviderRecordId + "\" AND contentProviderName_index: \""+contentProviderName+"\"");
        searchRequest.setLanguage(LanguageType.fromValue(Locale.ENGLISH.getLanguage()));
        searchRequest.setPage(1);
        searchRequest.setPageSize(1);
        return searchRequest;
    }
}
