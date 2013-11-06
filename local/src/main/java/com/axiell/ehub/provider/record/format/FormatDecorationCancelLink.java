package com.axiell.ehub.provider.record.format;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import com.axiell.ehub.provider.ContentProviderMediator;

final class FormatDecorationCancelLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProviderMediator contentProviderMediator;

    FormatDecorationCancelLink(final String id, final ContentProviderMediator contentProviderMediator) {
	super(id);
	this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
	contentProviderMediator.afterCancelNewFormatDecoration(target);
    }
}