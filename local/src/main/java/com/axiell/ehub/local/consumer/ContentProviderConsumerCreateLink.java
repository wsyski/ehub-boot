package com.axiell.ehub.local.consumer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import java.util.Optional;

final class ContentProviderConsumerCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ConsumersMediator consumersMediator;

    ContentProviderConsumerCreateLink(final String id, final ConsumersMediator consumersMediator) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick(final Optional<AjaxRequestTarget> targetOptional) {
        setVisible(false);

        targetOptional.ifPresent(target -> target.add(this));

        consumersMediator.afterNewContentProviderConsumerLinkClick(targetOptional);
    }
}
