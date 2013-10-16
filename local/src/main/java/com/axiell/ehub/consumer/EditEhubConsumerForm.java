package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.DisabledTextField;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

class EditEhubConsumerForm extends AbstractEhubConsumerForm {

    EditEhubConsumerForm(final String id, final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator) {
	super(id);
        addIdField(ehubConsumer);
        addSecretKeyField(ehubConsumer);
        addSubmitButton(consumersMediator, formModel);
    }

    private void addIdField(final EhubConsumer ehubConsumer) {
	final DisabledTextField idField = new DisabledTextField("id", ehubConsumer, "id");
        add(idField);
    }

    private void addSecretKeyField(final EhubConsumer ehubConsumer) {
	final DisabledTextField secretKeyField = new DisabledTextField("secretKey", ehubConsumer, "secretKey");
        add(secretKeyField);
    }
    
    private void addSubmitButton(final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
	final Button submitButton = new EditEhubConsumerSubmitButton("submit", consumersMediator, formModel);
        add(submitButton);
    }
    
    @Override
    protected TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(EhubConsumer ehubConsumer) {
	return new TranslatedKeys<>(this, ehubConsumer.getProperties());
    }
}
