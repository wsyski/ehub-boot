package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class ContentDispositionModel implements IModel<ContentDisposition> {
    private final IModel<FormatDecoration> formModel;

    ContentDispositionModel(final IModel<FormatDecoration> formModel) {
        this.formModel = formModel;
    }

    @Override
    public ContentDisposition getObject() {
        final FormatDecoration formatDecoration = formModel.getObject();
        return formatDecoration.getContentDisposition();
    }

    @Override
    public void setObject(ContentDisposition contentDisposition) {
        final FormatDecoration formatDecoration = formModel.getObject();
        formatDecoration.setContentDisposition(contentDisposition);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}