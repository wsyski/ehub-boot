package com.axiell.ehub.provider.record.format;

class FormatDecorationEditForm extends AbstractFormatDecorationForm<FormatDecorationMediator> {

    FormatDecorationEditForm(final String id, final FormatDecorationMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected boolean isNewFormatDecoration() {
        return false;
    }

    @Override
    protected AbstractFormatDecorationSaveButton<FormatDecorationMediator> makeSaveButton(String id) {
        return new FormatDecorationEditButton(id, formModel, mediator);
    }
}
