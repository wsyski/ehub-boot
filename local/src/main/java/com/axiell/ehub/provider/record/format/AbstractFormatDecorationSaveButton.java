package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

abstract class AbstractFormatDecorationSaveButton<M> extends Button {
    protected final M mediator;
    private final IModel<FormatDecoration> formModel;
    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    AbstractFormatDecorationSaveButton(final String id, final IModel<FormatDecoration> formModel, final M mediator) {
        super(id);
        this.formModel = formModel;
        this.mediator = mediator;
    }

    @Override
    public void onSubmit() {
        final FormatDecoration formatDecoration = formModel.getObject();
        final FormatDecoration savedFormatDecoration = formatAdminController.save(formatDecoration);
        afterSubmit(savedFormatDecoration);
    }

    protected abstract void afterSubmit(FormatDecoration savedFormatDecoration);
}
