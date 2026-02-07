package com.axiell.ehub.consumer;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderAdminController;

import java.io.Serializable;
import java.util.List;

class EhubConsumerHandler implements Serializable {
    private List<ContentProvider> remainingProviders;

    void retrieveRemainingContentProviders(final IContentProviderAdminController contentProviderAdminController, final EhubConsumer ehubConsumer) {
        List<ContentProvider> allContentProviders = contentProviderAdminController.getContentProviders();
        remainingProviders = ehubConsumer.getRemainingContentProviders(allContentProviders);
    }

    boolean hasRemainingProviders() {
        return !remainingProviders.isEmpty();
    }

    List<ContentProvider> getRemainingContentProviders() {
        return remainingProviders;
    }
}
