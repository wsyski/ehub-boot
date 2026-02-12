package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.local.provider.ContentProviderMediator;

class FormatDecorationCreateForm extends AbstractFormatDecorationForm<ContentProviderMediator> {

    FormatDecorationCreateForm(final String id, final ContentProviderMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected boolean isNewFormatDecoration() {
        return true;
    }

    @Override
    protected AbstractFormatDecorationSaveButton<ContentProviderMediator> makeSaveButton(String id) {
        return new FormatDecorationCreateButton(id, formModel, mediator);
    }
}
