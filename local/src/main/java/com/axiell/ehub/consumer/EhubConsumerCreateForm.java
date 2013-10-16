package com.axiell.ehub.consumer;

import java.util.Arrays;
import java.util.List;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

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
	final List<EhubConsumerPropertyKey> propertyKeys = Arrays.asList(EhubConsumer.EhubConsumerPropertyKey.values());
	return new TranslatedKeys<>(this, propertyKeys);
    }
}
