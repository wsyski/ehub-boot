package com.axiell.ehub.consumer;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertiesListView;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

class ContentProviderConsumerPropertiesListView extends AbstractPropertiesListView<ContentProviderConsumer, ContentProviderConsumerPropertyKey> {
    
    ContentProviderConsumerPropertiesListView(final String id, final IModel<ContentProviderConsumer> formModel) {
        super(id, formModel);
    }

    @Override
    protected IModel<String> makePropertyModel(ContentProviderConsumerPropertyKey propertyKey) {
        return new ContentProviderConsumerPropertyModel(formModel, propertyKey);
    }
}