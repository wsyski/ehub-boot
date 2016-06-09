package com.axiell.ehub.consumer;

import com.axiell.ehub.DisabledTextField;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import org.apache.wicket.markup.html.form.Button;

class EhubConsumerEditForm extends AbstractEhubConsumerForm {

    EhubConsumerEditForm(final String id, final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator) {
        super(id);
        addIdField(ehubConsumer);
        addSecretKeyField(ehubConsumer);
        addEditButton(consumersMediator);
    }

    private void addIdField(final EhubConsumer ehubConsumer) {
        final DisabledTextField idField = new DisabledTextField("id", ehubConsumer, "id");
        idField.setOutputMarkupId(true);
        add(idField);
    }

    private void addSecretKeyField(final EhubConsumer ehubConsumer) {
        final DisabledTextField secretKeyField = new DisabledTextField("secretKey", ehubConsumer, "secretKey");
        secretKeyField.setOutputMarkupId(true);
        add(secretKeyField);
    }

    private void addEditButton(final ConsumersMediator consumersMediator) {
        final Button submitButton = new EhubConsumerEditButton("submit", consumersMediator, formModel);
        add(submitButton);
    }

    @Override
    protected TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(EhubConsumer ehubConsumer) {
        return new TranslatedKeys<>(this, ehubConsumer.getProperties());
    }
}
