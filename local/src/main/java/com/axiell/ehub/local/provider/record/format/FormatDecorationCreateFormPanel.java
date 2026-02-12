package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.local.provider.ContentProviderMediator;

public class FormatDecorationCreateFormPanel extends AbstractFormatDecorationFormPanel<ContentProviderMediator> {

    public FormatDecorationCreateFormPanel(final String panelId, final ContentProviderMediator mediator) {
        super(panelId, mediator);
    }

    @Override
    protected FormatDecorationCancelLink makeFormatDecorationCancelLink(String id) {
        return new FormatDecorationCancelLink(id, mediator);
    }

    @Override
    protected AbstractFormatDecorationForm<ContentProviderMediator> makeFormatDecorationForm(String id) {
        return new FormatDecorationCreateForm(id, mediator);
    }
}
