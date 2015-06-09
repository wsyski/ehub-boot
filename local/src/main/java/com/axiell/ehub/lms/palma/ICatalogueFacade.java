/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.search.v267.service.SearchResponse;
import com.axiell.ehub.consumer.EhubConsumer;

public interface ICatalogueFacade {
    SearchResponse.SearchResult search(EhubConsumer ehubConsumer, String contentProviderName, String contentProviderId);
}
