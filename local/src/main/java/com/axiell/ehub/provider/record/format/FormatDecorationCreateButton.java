package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

import com.axiell.ehub.provider.ContentProviderMediator;

class FormatDecorationCreateButton extends AbstractFormatDecorationSaveButton<ContentProviderMediator> {

    FormatDecorationCreateButton(final String id, final IModel<FormatDecoration> formModel, final ContentProviderMediator mediator) {
	super(id, formModel, mediator);
    }
    
    @Override
    protected void afterSubmit(FormatDecoration savedFormatDecoration) {
	mediator.afterNewFormatDecoration(savedFormatDecoration);
    }
}
