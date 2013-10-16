package com.axiell.ehub.consumer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

final class ContentProviderConsumerCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ConsumersMediator consumersMediator;
    
    ContentProviderConsumerCreateLink(final String id, final ConsumersMediator consumersMediator) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.consumersMediator = consumersMediator;
    }
    
    @Override
    public void onClick(final AjaxRequestTarget target) {
        setVisible(false);

        if (target != null) {
            target.addComponent(this);
        }
        
        consumersMediator.afterNewContentProviderConsumerLinkClick(target);
    }
}