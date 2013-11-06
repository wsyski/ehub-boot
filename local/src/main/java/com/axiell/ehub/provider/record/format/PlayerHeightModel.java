package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class PlayerHeightModel extends AbstractPlayerModel {

    PlayerHeightModel(final IModel<FormatDecoration> formModel) {
	super(formModel);
    }

    @Override
    protected Integer getObject(final FormatDecoration formatDecoration) {
        return formatDecoration.getPlayerHeight();
    }
    
    @Override
    protected void setObject(final FormatDecoration formatDecoration, final Integer value) {
        formatDecoration.setPlayerHeight(value);
    }
}
