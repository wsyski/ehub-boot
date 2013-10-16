package com.axiell.ehub.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.ContentProvider;

/**
 * This {@link AjaxFormComponentUpdatingBehavior} updates the {@link ListView}
 * of {@link ContentProviderConsumer} properties when the {@link Component} this
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