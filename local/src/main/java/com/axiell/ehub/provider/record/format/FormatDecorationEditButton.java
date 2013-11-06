package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class FormatDecorationEditButton extends AbstractFormatDecorationSaveButton<FormatDecorationMediator> {

    FormatDecorationEditButton(final String id, final IModel<FormatDecoration> formModel, final FormatDecorationMediator mediator) {
	super(id, formModel, mediator);
    }
    
    @Override
    protected void afterSubmit(FormatDecoration savedFormatDecoration) {
	mediator.afterEditFormatDecoration();
    }
}
