package com.axiell.ehub.consumer;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import com.axiell.ehub.provider.ContentProvider;

class ContentProvidersModel extends LoadableDetachableModel<List<ContentProvider>> {    
    private final EhubConsumerHandler ehubConsumerHandler;
    
    ContentProvidersModel(final EhubConsumerHandler ehubConsumerHandler) {
	this.ehubConsumerHandler = ehubConsumerHandler;
    }

    @Override
    protected List<ContentProvider> load() {
        return ehubConsumerHandler.getRemainingContentProviders();
    }
}