package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.local.AbstractPropertiesListView;
import org.apache.wicket.model.IModel;

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
        ContentProvider contentProviderConsumer = formModel.getObject();
        return contentProviderConsumer.getPropertyValidatorPattern(propertyKey);
    }
}
