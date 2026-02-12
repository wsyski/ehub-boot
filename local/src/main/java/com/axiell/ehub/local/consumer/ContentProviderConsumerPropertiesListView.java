package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.local.AbstractPropertiesListView;
import org.apache.wicket.model.IModel;

import java.util.regex.Pattern;

class ContentProviderConsumerPropertiesListView extends AbstractPropertiesListView<ContentProviderConsumer, ContentProviderConsumerPropertyKey> {

    ContentProviderConsumerPropertiesListView(final String id, final IModel<ContentProviderConsumer> formModel) {
        super(id, formModel);
    }

    @Override
    protected IModel<String> makePropertyModel(final ContentProviderConsumerPropertyKey propertyKey) {
        return new ContentProviderConsumerPropertyModel(formModel, propertyKey);
    }

    @Override
    protected Pattern getPropertyValidatorPattern(final ContentProviderConsumerPropertyKey propertyKey) {
        ContentProviderConsumer contentProviderConsumer = formModel.getObject();
        return contentProviderConsumer.getPropertyValidatorPattern(propertyKey);
    }
}
