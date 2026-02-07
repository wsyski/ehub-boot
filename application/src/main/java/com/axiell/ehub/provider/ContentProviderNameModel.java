package com.axiell.ehub.provider;

import org.apache.wicket.model.IModel;

/**
 * The {@link IModel} for the description of an {@link ContentProvider}.
 */
class ContentProviderNameModel implements IModel<String> {
    private final IModel<ContentProvider> formModel;

    ContentProviderNameModel(final IModel<ContentProvider> formModel) {
        this.formModel = formModel;
    }

    @Override
    public String getObject() {
        final ContentProvider contentProvider = formModel.getObject();
        return contentProvider == null ? null : contentProvider.getName();
    }

    @Override
    public void setObject(final String value) {
        final ContentProvider contentProvider = formModel.getObject();
        contentProvider.setName(value);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}