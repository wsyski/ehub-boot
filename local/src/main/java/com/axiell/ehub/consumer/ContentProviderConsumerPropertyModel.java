package com.axiell.ehub.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertyModel;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

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