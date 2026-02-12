package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.local.AbstractPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link IModel} for a specific property of the {@link ContentProvider}.
 */
class ContentProviderPropertyModel extends AbstractPropertyModel<ContentProvider, ContentProviderPropertyKey> {

    ContentProviderPropertyModel(IModel<ContentProvider> formModel, ContentProviderPropertyKey propertyKey) {
        super(formModel, propertyKey);
    }

    @Override
    protected Map<ContentProviderPropertyKey, String> getProperties(ContentProvider contentProvider) {
        Map<ContentProviderPropertyKey, String> properties = contentProvider.getProperties();

        if (properties == null) {
            properties = new HashMap<ContentProviderPropertyKey, String>();
            contentProvider.setProperties(properties);
        }

        return properties;
    }
}
