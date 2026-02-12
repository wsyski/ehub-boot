package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

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
