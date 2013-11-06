package com.axiell.ehub.consumer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

final class EhubConsumerCancelLink extends IndicatingAjaxFallbackLink<Void> {
    private final ConsumersMediator consumersMediator;

    EhubConsumerCancelLink(final String id, final ConsumersMediator consumersMediator) {
        super(id);
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        consumersMediator.afterCancelNewEhubConsumerConsumer(target);
    }
}