package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.provider.ContentProvider;
import org.apache.wicket.markup.html.form.ChoiceRenderer;


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
        String contentProviderName = contentProvider.getName();
        return contentProviderName.toString();
    }
}
