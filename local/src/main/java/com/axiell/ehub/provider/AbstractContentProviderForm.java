package com.axiell.ehub.provider;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

abstract class AbstractContentProviderForm extends StatelessForm<ContentProvider> {
    protected IModel<ContentProvider> formModel;
    protected final ListView<TranslatedKey<ContentProvider.ContentProviderPropertyKey>> contentProviderPropertiesListView;

    AbstractContentProviderForm(final String id) {
        super(id);

        formModel = new Model<>();
        setModel(formModel);

        contentProviderPropertiesListView = new ContentProviderPropertiesListView("properties", formModel);
        add(contentProviderPropertiesListView);
    }


    void setContentProvider(final ContentProvider contentProvider) {
        setModelObject(contentProvider);
        TranslatedKeys<ContentProvider.ContentProviderPropertyKey> propertyKeys = getPropertyKeys(contentProvider);
        setPropertyKeys(propertyKeys);
    }

    private TranslatedKeys<ContentProvider.ContentProviderPropertyKey> getPropertyKeys(final ContentProvider contentProvider) {
        final List<ContentProvider.ContentProviderPropertyKey> propertyKeys = contentProvider.getValidPropertyKeys();
        return new TranslatedKeys<>(this, propertyKeys);
    }

    private void setPropertyKeys(TranslatedKeys<ContentProvider.ContentProviderPropertyKey> propertyKeys) {
        contentProviderPropertiesListView.setList(propertyKeys);
    }
}

