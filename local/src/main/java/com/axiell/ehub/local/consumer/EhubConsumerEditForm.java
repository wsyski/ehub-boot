package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.local.DisabledTextField;
import com.axiell.ehub.local.TranslatedKeys;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

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
        final TextField<String> secretKeyField = new TextField<>("secretKey", new PropertyModel<String>(formModel, "secretKey"));
        secretKeyField.add(new SecretKeyValidator());
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
