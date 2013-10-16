package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

abstract class AbstractContentProviderConsumerForm extends StatelessForm<ContentProviderConsumer> {
    protected final IModel<ContentProviderConsumer> formModel;
    protected final ContentProviderConsumerPropertiesListView contentProviderConsumerPropertiesListView;

    AbstractContentProviderConsumerForm(String id) {
	super(id);
	formModel = new Model<>();
	setModel(formModel);
	
	contentProviderConsumerPropertiesListView = new ContentProviderConsumerPropertiesListView("cpcProperties", formModel);
    }
}
