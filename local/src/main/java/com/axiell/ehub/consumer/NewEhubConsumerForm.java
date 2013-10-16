package com.axiell.ehub.consumer;

import java.util.Arrays;
import java.util.List;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

class NewEhubConsumerForm extends AbstractEhubConsumerForm {

    NewEhubConsumerForm(final String id, final ConsumersMediator consumersMediator) {
	super(id);
	addSubmitButton(consumersMediator);
    }

    private void addSubmitButton(final ConsumersMediator consumersMediator) {
	final NewEhubConsumerSubmitButton submitButton = new NewEhubConsumerSubmitButton("submit", consumersMediator, formModel);
	add(submitButton);
    }

    @Override
    protected TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(EhubConsumer ehubConsumer) {
	final List<EhubConsumerPropertyKey> propertyKeys = Arrays.asList(EhubConsumer.EhubConsumerPropertyKey.values());
	return new TranslatedKeys<>(this, propertyKeys);
    }
}
