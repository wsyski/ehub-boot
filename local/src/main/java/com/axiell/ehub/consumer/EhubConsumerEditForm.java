package com.axiell.ehub.consumer;

import com.axiell.ehub.DisabledTextField;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.language.Language;
import com.axiell.ehub.language.LanguageChoice;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

class EhubConsumerEditForm extends AbstractEhubConsumerForm {

    EhubConsumerEditForm(final String id, final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator) {
        super(id);
        addIdField(ehubConsumer);
        addSecretKeyField(ehubConsumer);
        addDefaultLanguage(ehubConsumer);
        addEditButton(consumersMediator, formModel);
    }

    private void addIdField(final EhubConsumer ehubConsumer) {
        final DisabledTextField idField = new DisabledTextField("id", ehubConsumer, "id");
        add(idField);
    }

    private void addSecretKeyField(final EhubConsumer ehubConsumer) {
        final DisabledTextField secretKeyField = new DisabledTextField("secretKey", ehubConsumer, "secretKey");
        add(secretKeyField);
    }

    private void addDefaultLanguage(final EhubConsumer ehubConsumer) {
        final LanguageChoice languageChoice = new LanguageChoice("defaultLanguage", new PropertyModel<Language>(ehubConsumer, "defaultLanguage"));
        add(languageChoice);
    }

    private void addEditButton(final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
        final Button submitButton = new EhubConsumerEditButton("submit", consumersMediator, formModel);
        add(submitButton);
    }

    @Override
    protected TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(EhubConsumer ehubConsumer) {
        return new TranslatedKeys<>(this, ehubConsumer.getProperties());
    }
}
