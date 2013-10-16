package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;

/**
 * Provides the possibility to render a {@link ContentProvider}.
 */
class ContentProviderChoiceRenderer extends ChoiceRenderer<ContentProvider> {

    @Override
    public String getIdValue(final ContentProvider contentProvider, final int index) {
        return contentProvider.getId().toString();
    }

    @Override
    public Object getDisplayValue(ContentProvider contentProvider) {
        ContentProviderName contentProviderName = contentProvider.getName();
        return contentProviderName.toString();
    }
}