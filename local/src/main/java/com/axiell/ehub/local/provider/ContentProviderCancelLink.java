package com.axiell.ehub.local.provider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import java.util.Optional;

final class ContentProviderCancelLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProvidersMediator contentProvidersMediator;

    ContentProviderCancelLink(final String id, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        this.contentProvidersMediator = contentProvidersMediator;
    }

    @Override
    public void onClick(final Optional<AjaxRequestTarget> targetOptional) {
        contentProvidersMediator.afterCancelNewContentProvider(targetOptional);
    }
}
