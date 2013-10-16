package com.axiell.ehub.consumer;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

class NewContentProviderConsumerForm extends AbstractContentProviderConsumerForm {

    NewContentProviderConsumerForm(final String id, final EhubConsumerHandler ehubConsumerHandler, final EhubConsumer ehubConsumer,
	    final ConsumersMediator consumersMediator) {
	super(id);
	consumersMediator.registerContentProviderConsumerPropertiesListView(contentProviderConsumerPropertiesListView);

	addContentProviderChoice(ehubConsumerHandler, consumersMediator);
	addContentProviderConsumerPropertiesContainer(consumersMediator);
	addSubmitButton(ehubConsumer, consumersMediator, formModel);
    }

    private void addContentProviderChoice(final EhubConsumerHandler ehubConsumerHandler, final ConsumersMediator consumersMediator) {
	final ContentProviderDropDownChoice contentProviderChoice = new ContentProviderDropDownChoice("contentProvider", consumersMediator, formModel, ehubConsumerHandler);
	add(contentProviderChoice);
    }

    private void addContentProviderConsumerPropertiesContainer(final ConsumersMediator consumersMediator) {
	final WebMarkupContainer contentProviderConsumerPropertiesContainer = makeContentProviderConsumerPropertiesContainer();
	consumersMediator.registerContentProviderConsumerPropertiesContainer(contentProviderConsumerPropertiesContainer);
	add(contentProviderConsumerPropertiesContainer);
    }

    private WebMarkupContainer makeContentProviderConsumerPropertiesContainer() {
	WebMarkupContainer container = new WebMarkupContainer("cpcPropertiesContainer");
	container.setOutputMarkupId(true);
	container.add(contentProviderConsumerPropertiesListView);
	return container;
    }

    private void addSubmitButton(final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
	final Button submitButton = new NewContentProviderConsumerSubmitButton("submit", formModel, consumersMediator, ehubConsumer);
	add(submitButton);
    }

    void resetForm() {
	final ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
	setModelObject(contentProviderConsumer);
	final List<TranslatedKey<ContentProviderConsumerPropertyKey>> propertyKeys = Collections.emptyList();
	contentProviderConsumerPropertiesListView.setList(propertyKeys);
    }
}
