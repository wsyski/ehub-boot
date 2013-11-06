package com.axiell.ehub.provider.record.format;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import com.axiell.ehub.provider.ContentProviderMediator;

public final class FormatDecorationCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProviderMediator contentProviderMediator;

    public FormatDecorationCreateLink(final String id, final ContentProviderMediator contentProviderMediator) {
        super(id);
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        setVisible(false);

        if (target != null) {
            target.addComponent(this);
        }
        
        contentProviderMediator.afterClickOnFormatDecorationCreateLink(target);
    }
}