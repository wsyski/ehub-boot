package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.apache.wicket.model.IModel;

/**
 * The {@link IModel} for the description of an {@link EhubConsumer}.
 */
class EhubConsumerDescriptionModel implements IModel<String> {
    private final IModel<EhubConsumer> formModel;

    EhubConsumerDescriptionModel(final IModel<EhubConsumer> formModel) {
        this.formModel = formModel;
    }

    @Override
    public String getObject() {
        final EhubConsumer ehubConsumer = formModel.getObject();
        return ehubConsumer.getDescription();
    }

    @Override
    public void setObject(final String value) {
        final EhubConsumer ehubConsumer = formModel.getObject();
        ehubConsumer.setDescription(value);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
