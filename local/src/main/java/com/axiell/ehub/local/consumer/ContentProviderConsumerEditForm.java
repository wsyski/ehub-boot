package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.local.TranslatedKeys;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

class ContentProviderConsumerEditForm extends AbstractContentProviderConsumerForm {

    ContentProviderConsumerEditForm(final String id, final ContentProviderConsumer contentProviderConsumer, final ConsumersMediator consumersMediator) {
        super(id);
        addPropertiesListView(contentProviderConsumer);
        addEditButton(consumersMediator, formModel);
    }

    private void addPropertiesListView(final ContentProviderConsumer contentProviderConsumer) {
        final TranslatedKeys<ContentProviderConsumerPropertyKey> keys = new TranslatedKeys<>(this, contentProviderConsumer.getProperties());
        contentProviderConsumerPropertiesListView.setList(keys);
        add(contentProviderConsumerPropertiesListView);
    }

    private void addEditButton(final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
        final Button button = new ContentProviderConsumerEditButton("submit", consumersMediator, formModel);
        add(button);
    }
}
