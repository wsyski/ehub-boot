package com.axiell.ehub.provider;

import java.util.List;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

class ContentProviderEditForm extends StatelessForm<ContentProvider> {
    private final ContentProviderPropertiesListView contentProviderPropertiesListView;

    ContentProviderEditForm(final String id, final ContentProviderMediator contentProviderMediator) {
	super(id);
	final IModel<ContentProvider> formModel = new Model<>();
	setModel(formModel);
	
	contentProviderPropertiesListView = new ContentProviderPropertiesListView("properties", formModel);
        add(contentProviderPropertiesListView);
	
        addEditButton(contentProviderMediator, formModel);
    }

    private void addEditButton(final ContentProviderMediator contentProviderMediator, final IModel<ContentProvider> formModel) {
	final ContentProviderEditButton editButton = new ContentProviderEditButton("submit", formModel, contentProviderMediator);
        add(editButton);
    }
    
    void setContentProvider(final ContentProvider contentProvider) {
	setModelObject(contentProvider);	
	setPropertyKeys(contentProvider);
    }

    private void setPropertyKeys(final ContentProvider contentProvider) {
	final List<ContentProviderPropertyKey> propertyKeys = contentProvider.getValidPropertyKeys();
        final TranslatedKeys<ContentProviderPropertyKey> translatedKeys = new TranslatedKeys<>(this, propertyKeys);
        contentProviderPropertiesListView.setList(translatedKeys);
    }
}
