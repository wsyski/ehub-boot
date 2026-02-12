package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.model.IModel;

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
