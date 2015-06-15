package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.search.v267.searchrequest.*;
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
    private static final com.axiell.arena.services.palma.search.v267.service.ObjectFactory SERVICE_OBJECT_FACTORY =new com.axiell.arena.services.palma.search.v267.service.ObjectFactory();
    private static final com.axiell.arena.services.palma.search.v267.searchrequest.ObjectFactory SEARCHREQUEST_OBJECT_FACTORY =new com.axiell.arena.services.palma.search.v267.searchrequest.ObjectFactory();


    @Autowired
    private ICataloguePortFactory cataloguePortFactory;

    @Override
    public SearchResponse.SearchResult search(final EhubConsumer ehubConsumer, String contentProviderAlias, final String contentProviderRecordId) {
        Search.SearchRequest searchRequest = createSearchRequest(ehubConsumer, contentProviderAlias, contentProviderRecordId);
        Catalogue catalogue = cataloguePortFactory.getInstance(ehubConsumer);
        return catalogue.search(searchRequest);
    }

    private static Search.SearchRequest createSearchRequest(final EhubConsumer ehubConsumer, String contentProviderAlias, final String contentProviderRecordId) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        Search.SearchRequest searchRequest = SERVICE_OBJECT_FACTORY.createSearchSearchRequest();
        searchRequest.setArenaMember(agencyMemberIdentifier);
        searchRequest.setQuery("contentProviderRecordId_index: \"" + contentProviderRecordId + "\" AND contentProviderName_index: \""+ contentProviderAlias +"\"");
        searchRequest.setLanguage(LanguageType.fromValue(Locale.ENGLISH.getLanguage()));
        searchRequest.setPage(1);
        searchRequest.setPageSize(1);
        Availability availability=SEARCHREQUEST_OBJECT_FACTORY.createAvailability();
        availability.setEnable("no");
        searchRequest.setAvailability(availability);
        Covers covers=SEARCHREQUEST_OBJECT_FACTORY.createCovers();
        covers.setEnable("no");
        searchRequest.setCovers(covers);
        Facets facets=SEARCHREQUEST_OBJECT_FACTORY.createFacets();
        facets.setEnable("no");
        searchRequest.setFacets(facets);
        Ratings ratings=SEARCHREQUEST_OBJECT_FACTORY.createRatings();
        ratings.setEnable("no");
        searchRequest.setRatings(ratings);
        RatingAverage ratingAverage=SEARCHREQUEST_OBJECT_FACTORY.createRatingAverage();
        ratingAverage.setEnable("no");
        searchRequest.setRatingAverage(ratingAverage);
        Reviews reviews=SEARCHREQUEST_OBJECT_FACTORY.createReviews();
        reviews.setEnable("no");
        searchRequest.setReviews(reviews);
        Tags tags=SEARCHREQUEST_OBJECT_FACTORY.createTags();
        tags.setEnable("no");
        tags.setCount(0);
        searchRequest.setTags(tags);
        return searchRequest;
    }
}
