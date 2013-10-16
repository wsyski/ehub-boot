package com.axiell.ehub.consumer;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertiesListView;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

class EhubConsumerPropertiesListView extends AbstractPropertiesListView<EhubConsumer, EhubConsumerPropertyKey> {
    
    EhubConsumerPropertiesListView(String id, IModel<EhubConsumer> formModel) {
        super(id, formModel);
    }
    
    @Override
    protected IModel<String> makePropertyModel(EhubConsumerPropertyKey propertyKey) {
        return new EhubConsumerPropertyModel(formModel, propertyKey);
    }
}