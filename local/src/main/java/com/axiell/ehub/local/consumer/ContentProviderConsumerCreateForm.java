package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.local.TranslatedKey;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import java.util.Collections;
import java.util.List;

class ContentProviderConsumerCreateForm extends AbstractContentProviderConsumerForm {

    ContentProviderConsumerCreateForm(final String id, final EhubConsumerHandler ehubConsumerHandler, final EhubConsumer ehubConsumer,
                                      final ConsumersMediator consumersMediator) {
        super(id);
        consumersMediator.registerContentProviderConsumerPropertiesListView(contentProviderConsumerPropertiesListView);

        addContentProviderChoice(ehubConsumerHandler, consumersMediator);
        addContentProviderConsumerPropertiesContainer(consumersMediator);
        addCreateButton(ehubConsumer, consumersMediator, formModel);
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

    private void addCreateButton(final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
        final Button createButton = new ContentProviderConsumerCreateButton("submit", formModel, consumersMediator, ehubConsumer);
        add(createButton);
    }

    void resetForm() {
        final ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        setModelObject(contentProviderConsumer);
        final List<TranslatedKey<ContentProviderConsumerPropertyKey>> propertyKeys = Collections.emptyList();
        contentProviderConsumerPropertiesListView.setList(propertyKeys);
    }
}
