package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.TranslatedKeys;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This AjaxFormComponentUpdatingBehavior updates the ListView
 * of ContentProviderConsumer properties when the Component this
 * behavior is added to is changed.
 */
class ContentProviderChoiceOnChangeBehavior extends AjaxFormComponentUpdatingBehavior {
    private final IModel<ContentProviderConsumer> formModel;
    private final ConsumersMediator consumersMediator;

    ContentProviderChoiceOnChangeBehavior(final IModel<ContentProviderConsumer> formModel, final ConsumersMediator consumersMediator) {
        super("onchange");
        this.formModel = formModel;
        this.consumersMediator = consumersMediator;
    }

    @Override
    protected void onUpdate(final AjaxRequestTarget target) {
        final TranslatedKeys<ContentProviderConsumerPropertyKey> translatedKeys = getTransaltedKeys();
        consumersMediator.onSelectedContentProviderConsumerChanged(translatedKeys, target);
    }

    private TranslatedKeys<ContentProviderConsumerPropertyKey> getTransaltedKeys() {
        final Form<?> form = getForm();
        final List<ContentProviderConsumerPropertyKey> keys = getPropertyKeys();
        return new TranslatedKeys<>(form, keys);
    }

    private Form<?> getForm() {
        final FormComponent<?> formComponent = getFormComponent();
        return formComponent.getForm();
    }

    private List<ContentProviderConsumerPropertyKey> getPropertyKeys() {
        final ContentProviderConsumer contentProviderConsumer = formModel.getObject();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return contentProvider == null ? new ArrayList<ContentProviderConsumerPropertyKey>() : contentProviderConsumer.getValidPropertyKeys();
    }
}
