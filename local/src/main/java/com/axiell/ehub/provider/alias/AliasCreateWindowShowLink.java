package com.axiell.ehub.provider.alias;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

class AliasCreateWindowShowLink extends IndicatingAjaxFallbackLink<Void> {
    private final AliasMediator mediator;

    AliasCreateWindowShowLink(final String id, final AliasMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    public void onClick(final AjaxRequestTarget target) {
        mediator.showCreateWindow(target);
    }
}
