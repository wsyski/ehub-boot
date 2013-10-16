package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

class ContentProviderConsumerEditForm extends AbstractContentProviderConsumerForm {

    ContentProviderConsumerEditForm(final String id, final ContentProviderConsumer contentProviderConsumer, final ConsumersMediator consumersMediator) {
	super(id);
	addPropertiesListView(contentProviderConsumer, formModel);
	addEditButton(consumersMediator, formModel);
    }

    private void addPropertiesListView(final ContentProviderConsumer contentProviderConsumer, final IModel<ContentProviderConsumer> formModel) {
	final TranslatedKeys<ContentProviderConsumerPropertyKey> keys = new TranslatedKeys<>(this, contentProviderConsumer.getProperties());
	contentProviderConsumerPropertiesListView.setList(keys);
	add(contentProviderConsumerPropertiesListView);
    }

    private void addEditButton(final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
	final Button button = new ContentProviderConsumerEditButton("submit", consumersMediator, formModel);
	add(button);
    }
}
