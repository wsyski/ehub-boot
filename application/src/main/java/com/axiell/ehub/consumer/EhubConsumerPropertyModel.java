package com.axiell.ehub.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertyModel;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

/**
 * The {@link IModel} for a specific property of the {@link EhubConsumer}.
 */
class EhubConsumerPropertyModel extends AbstractPropertyModel<EhubConsumer, EhubConsumerPropertyKey> {

    EhubConsumerPropertyModel(final IModel<EhubConsumer> formModel, final EhubConsumerPropertyKey propertyKey) {
	super(formModel, propertyKey);
    }

    @Override
    protected Map<EhubConsumerPropertyKey, String> getProperties(EhubConsumer ehubConsumer) {
	Map<EhubConsumerPropertyKey, String> properties = ehubConsumer.getProperties();

	if (properties == null) {
	    properties = new HashMap<EhubConsumerPropertyKey, String>();
	    ehubConsumer.setProperties(properties);
	}
	return properties;
    }
}