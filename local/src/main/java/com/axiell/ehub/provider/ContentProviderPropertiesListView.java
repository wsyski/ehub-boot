package com.axiell.ehub.provider;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.AbstractPropertiesListView;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

import java.util.regex.Pattern;

class ContentProviderPropertiesListView extends AbstractPropertiesListView<ContentProvider, ContentProviderPropertyKey> {
    
    ContentProviderPropertiesListView(final String id, IModel<ContentProvider> formModel) {
        super(id, formModel);
    }
    
    @Override
    protected IModel<String> makePropertyModel(ContentProviderPropertyKey propertyKey) {
        return new ContentProviderPropertyModel(formModel, propertyKey);
    }

    @Override
    protected Pattern getPropertyValidatorPattern(final ContentProviderPropertyKey propertyKey) {
        ContentProvider contentProviderConsumer=formModel.getObject();
        return contentProviderConsumer.getPropertyValidatorPattern(propertyKey);
    }
}