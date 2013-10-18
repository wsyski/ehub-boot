package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class FormatIdModel implements IModel<String> {

    private final IModel<FormatDecoration> formModel;

    FormatIdModel(final IModel<FormatDecoration> formModel) {
	this.formModel = formModel;
    }
    
    @Override
    public String getObject() {
	FormatDecoration formatDecoration = formModel.getObject();
        return formatDecoration.getContentProviderFormatId();
    }
    
    @Override
    public void setObject(String value) {
	FormatDecoration formatDecoration = formModel.getObject();
	formatDecoration.setContentProviderFormatId(value);
    }
    
    @Override
    public void detach() {
        formModel.detach();
    }
}
