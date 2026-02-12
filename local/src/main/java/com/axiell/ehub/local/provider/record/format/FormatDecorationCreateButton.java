package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.provider.ContentProviderMediator;
import org.apache.wicket.model.IModel;

class FormatDecorationCreateButton extends AbstractFormatDecorationSaveButton<ContentProviderMediator> {

    FormatDecorationCreateButton(final String id, final IModel<FormatDecoration> formModel, final ContentProviderMediator mediator) {
        super(id, formModel, mediator);
    }

    @Override
    protected void afterSubmit(FormatDecoration savedFormatDecoration) {
        mediator.afterNewFormatDecoration(savedFormatDecoration);
    }
}
