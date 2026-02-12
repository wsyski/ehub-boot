package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
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


    @Override
    protected void onError() {
        // before updating, call the interception method for clients
        beforeUpdateFormComponentModels();
        // Update model using form data
        updateFormComponentModels();
        super.onError();
    }
}
