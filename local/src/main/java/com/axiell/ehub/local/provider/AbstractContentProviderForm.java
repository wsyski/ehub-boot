package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.local.TranslatedKey;
import com.axiell.ehub.local.TranslatedKeys;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

abstract class AbstractContentProviderForm extends StatelessForm<ContentProvider> {
    protected IModel<ContentProvider> formModel;
    private final ListView<TranslatedKey<ContentProvider.ContentProviderPropertyKey>> contentProviderPropertiesListView;

    AbstractContentProviderForm(final String id) {
        super(id);
        formModel = new Model<>();
        setModel(formModel);
        addIsLoanPerProductField();
        contentProviderPropertiesListView = new ContentProviderPropertiesListView("properties", formModel);
        add(contentProviderPropertiesListView);
    }

    void setContentProvider(final ContentProvider contentProvider) {
        setModelObject(contentProvider);
        TranslatedKeys<ContentProvider.ContentProviderPropertyKey> propertyKeys = getPropertyKeys(contentProvider);
        setPropertyKeys(propertyKeys);
    }

    private void addIsLoanPerProductField() {
        final ContentProviderIsLoanPerProductModel isLoanPerProductModel = new ContentProviderIsLoanPerProductModel(formModel);
        final CheckBox isLoanPerProductField = new CheckBox("isLoanPerProduct", isLoanPerProductModel);
        isLoanPerProductField.setOutputMarkupPlaceholderTag(true);
        add(isLoanPerProductField);
    }

    private TranslatedKeys<ContentProvider.ContentProviderPropertyKey> getPropertyKeys(final ContentProvider contentProvider) {
        final List<ContentProvider.ContentProviderPropertyKey> propertyKeys = contentProvider.getValidPropertyKeys();
        return new TranslatedKeys<>(this, propertyKeys);
    }

    private void setPropertyKeys(TranslatedKeys<ContentProvider.ContentProviderPropertyKey> propertyKeys) {
        contentProviderPropertiesListView.setList(propertyKeys);
    }

    @Override
    protected void onError() {
        // before updating, call the interception method for clients
        beforeUpdateFormComponentModels();
        // Update model using form data
        updateFormComponentModels();
        super.onError();
    }
}

