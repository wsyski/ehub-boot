package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.local.AbstractPropertiesListView;
import org.apache.wicket.model.IModel;

import java.util.regex.Pattern;

class EhubConsumerPropertiesListView extends AbstractPropertiesListView<EhubConsumer, EhubConsumerPropertyKey> {

    EhubConsumerPropertiesListView(String id, IModel<EhubConsumer> formModel) {
        super(id, formModel);
    }

    @Override
    protected IModel<String> makePropertyModel(EhubConsumerPropertyKey propertyKey) {
        return new EhubConsumerPropertyModel(formModel, propertyKey);
    }

    @Override
    protected Pattern getPropertyValidatorPattern(final EhubConsumerPropertyKey propertyKey) {
        EhubConsumer ehubConsumer = formModel.getObject();
        return ehubConsumer.getPropertyValidatorPattern(propertyKey);
    }
}
