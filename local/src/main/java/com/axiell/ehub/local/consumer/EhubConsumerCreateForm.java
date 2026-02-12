package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.local.TranslatedKeys;

import java.util.Arrays;
import java.util.List;

class EhubConsumerCreateForm extends AbstractEhubConsumerForm {

    EhubConsumerCreateForm(final String id, final ConsumersMediator consumersMediator) {
        super(id);
        addCreateButton(consumersMediator);
    }

    private void addCreateButton(final ConsumersMediator consumersMediator) {
        final EhubConsumerCreateButton button = new EhubConsumerCreateButton("submit", consumersMediator, formModel);
        add(button);
    }

    @Override
    protected TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(EhubConsumer ehubConsumer) {
        final List<EhubConsumerPropertyKey> propertyKeys = Arrays.asList(EhubConsumerPropertyKey.values());
        return new TranslatedKeys<>(this, propertyKeys);
    }
}
