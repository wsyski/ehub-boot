package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.local.AbstractPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link IModel} for a specific property of the {@link ContentProviderConsumer}.
 */
class ContentProviderConsumerPropertyModel extends AbstractPropertyModel<ContentProviderConsumer, ContentProviderConsumerPropertyKey> {

    ContentProviderConsumerPropertyModel(final IModel<ContentProviderConsumer> formModel, final ContentProviderConsumerPropertyKey propertyKey) {
        super(formModel, propertyKey);
    }

    @Override
    protected Map<ContentProviderConsumerPropertyKey, String> getProperties(ContentProviderConsumer contentProviderConsumer) {
        Map<ContentProviderConsumerPropertyKey, String> properties = contentProviderConsumer.getProperties();

        if (properties == null) {
            properties = new HashMap<ContentProviderConsumerPropertyKey, String>();
            contentProviderConsumer.setProperties(properties);
        }

        return properties;
    }
}
