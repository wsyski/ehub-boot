package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.local.provider.ContentProviderMediator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import java.util.Optional;

final class FormatDecorationCancelLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProviderMediator contentProviderMediator;

    FormatDecorationCancelLink(final String id, final ContentProviderMediator contentProviderMediator) {
        super(id);
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick(final Optional<AjaxRequestTarget> targetOptional) {
        contentProviderMediator.afterCancelNewFormatDecoration(targetOptional);
    }
}
