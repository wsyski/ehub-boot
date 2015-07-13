package com.axiell.ehub.consumer;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.language.LanguageChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

abstract class AbstractEhubConsumerForm extends StatelessForm<EhubConsumer> {
    protected IModel<EhubConsumer> formModel;
    protected final ListView<TranslatedKey<EhubConsumerPropertyKey>> ehubConsumerPropertiesListView;

    @SpringBean(name = "consumerAdminController")
    protected IConsumerAdminController consumerAdminController;

    AbstractEhubConsumerForm(String id) {
        super(id);

        formModel = new Model<>();
        setModel(formModel);

        addDescriptionField(formModel);
        addDefaultLanguage();

        ehubConsumerPropertiesListView = new EhubConsumerPropertiesListView("ecProperties", formModel);
        add(ehubConsumerPropertiesListView);
    }

    private void addDescriptionField(final IModel<EhubConsumer> formModel) {
        final EhubConsumerDescriptionModel descriptionModel = new EhubConsumerDescriptionModel(formModel);
        final RequiredTextField<String> descriptionField = new RequiredTextField<>("description", descriptionModel);
        add(descriptionField);
    }

    private void addDefaultLanguage() {
        final LanguageChoice languageChoice = new LanguageChoice("defaultLanguage", new EhubConsumerLanguageModel(formModel));
        add(languageChoice);
    }

    void setEhubConsumer(final EhubConsumer ehubConsumer) {
        setModelObject(ehubConsumer);
        TranslatedKeys<EhubConsumerPropertyKey> propertyKeys = getPropertyKeys(ehubConsumer);
        setPropertyKeys(propertyKeys);
    }

    protected abstract TranslatedKeys<EhubConsumerPropertyKey> getPropertyKeys(final EhubConsumer ehubConsumer);

    private void setPropertyKeys(TranslatedKeys<EhubConsumerPropertyKey> propertyKeys) {
        ehubConsumerPropertiesListView.setList(propertyKeys);
    }

    @Override
    protected void onError() {
        // before updating, call the interception method for clients
        beforeUpdateFormComponentModels();
        // Update model using form data
        updateFormComponentModels();
        super.onError();
    }
}

