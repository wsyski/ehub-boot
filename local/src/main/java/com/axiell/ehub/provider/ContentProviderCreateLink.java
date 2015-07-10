package com.axiell.ehub.provider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

final class ContentProviderCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProvidersMediator contentProvidersMediator;

    ContentProviderCreateLink(final String id, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.contentProvidersMediator = contentProvidersMediator;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        setVisible(false);

        if (target != null) {
            target.addComponent(this);
        }
        
        contentProvidersMediator.afterClickOnContentProviderCreateLink(target);
    }
}