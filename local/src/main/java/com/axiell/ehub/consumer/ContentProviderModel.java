package com.axiell.ehub.consumer;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.provider.ContentProvider;

class ContentProviderModel implements IModel<ContentProvider> {
    private final IModel<ContentProviderConsumer> formModel;

    ContentProviderModel(final IModel<ContentProviderConsumer> formModel) {
        this.formModel = formModel;
    }

    @Override
    public ContentProvider getObject() {
        final ContentProviderConsumer contentProviderConsumer = formModel.getObject();
        return contentProviderConsumer.getContentProvider();
    }

    @Override
    public void setObject(final ContentProvider contentProvider) {
	final ContentProviderConsumer contentProviderConsumer = formModel.getObject();
        contentProviderConsumer.setContentProvider(contentProvider);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}