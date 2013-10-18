package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

abstract class AbstractPlayerModel implements IModel<Integer> {
    private final IModel<FormatDecoration> formModel;

    AbstractPlayerModel(final IModel<FormatDecoration> formModel) {
	this.formModel = formModel;
    }
    
    @Override
    public final Integer getObject() {
        FormatDecoration formatDecoration = formModel.getObject();
        return getObject(formatDecoration);
    }
    
    protected abstract Integer getObject(FormatDecoration formatDecoration);
    
    @Override
    public final void setObject(Integer value) {
	FormatDecoration formatDecoration = formModel.getObject();
        setObject(formatDecoration, value);
    }
    
    protected abstract void setObject(FormatDecoration formatDecoration, Integer value);
    
    @Override
    public void detach() {
        formModel.detach();
    }
}
