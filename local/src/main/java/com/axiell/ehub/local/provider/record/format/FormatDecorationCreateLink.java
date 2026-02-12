package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.local.provider.ContentProviderMediator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;

import java.util.Optional;

public final class FormatDecorationCreateLink extends IndicatingAjaxFallbackLink<Void> {
    private final ContentProviderMediator contentProviderMediator;

    public FormatDecorationCreateLink(final String id, final ContentProviderMediator contentProviderMediator) {
        super(id);
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick(final Optional<AjaxRequestTarget> targetOptional) {
        setVisible(false);

        targetOptional.ifPresent(
                target -> target.add(this)
        );
        contentProviderMediator.afterClickOnFormatDecorationCreateLink(targetOptional);

    }
}
