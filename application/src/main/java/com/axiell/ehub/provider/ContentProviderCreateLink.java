package com.axiell.ehub.provider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import java.util.Optional;

final class ContentProviderCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProvidersMediator contentProvidersMediator;

    ContentProviderCreateLink(final String id, final ContentProvidersMediator contentProvidersMediator) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.contentProvidersMediator = contentProvidersMediator;
    }

    @Override
    public void onClick(final Optional<AjaxRequestTarget> targetOptional) {
        setVisible(false);

        targetOptional.ifPresent(
                target -> {
            target.add(this);
        });

        contentProvidersMediator.afterClickOnContentProviderCreateLink(targetOptional);
    }
}
