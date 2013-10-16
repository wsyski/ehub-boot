package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

class EditContentProviderConsumerForm extends AbstractContentProviderConsumerForm {

    EditContentProviderConsumerForm(final String id, final ContentProviderConsumer contentProviderConsumer, final ConsumersMediator consumersMediator) {
	super(id);
	addPropertiesListView(contentProviderConsumer, formModel);
	addSubmitButton(consumersMediator, formModel);
    }

    private void addPropertiesListView(final ContentProviderConsumer contentProviderConsumer, final IModel<ContentProviderConsumer> formModel) {
	final TranslatedKeys<ContentProviderConsumerPropertyKey> keys = new TranslatedKeys<>(this, contentProviderConsumer.getProperties());
	contentProviderConsumerPropertiesListView.setList(keys);
	add(contentProviderConsumerPropertiesListView);
    }

    private void addSubmitButton(final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
	final Button submitButton = new EditContentProviderConsumerSubmitButton("submit", consumersMediator, formModel);
	add(submitButton);
    }
}
