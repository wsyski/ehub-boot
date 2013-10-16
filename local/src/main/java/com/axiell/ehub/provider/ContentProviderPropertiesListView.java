package com.axiell.ehub.provider;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertiesListView;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

class ContentProviderPropertiesListView extends AbstractPropertiesListView<ContentProvider, ContentProviderPropertyKey> {
    
    ContentProviderPropertiesListView(final String id, IModel<ContentProvider> formModel) {
        super(id, formModel);
    }
    
    @Override
    protected IModel<String> makePropertyModel(ContentProviderPropertyKey propertyKey) {
        return new ContentProviderPropertyModel(formModel, propertyKey);
    }
}