package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class PlayerHeightModel extends AbstractPlayerModel {

    PlayerHeightModel(final IModel<FormatDecoration> formModel) {
	super(formModel);
    }

    @Override
    protected Integer getObject(FormatDecoration formatDecoration) {
        return formatDecoration.getPlayerHeight();
    }
    
    @Override
    protected void setObject(FormatDecoration formatDecoration, Integer value) {
        formatDecoration.setPlayerHeight(value);
    }
}
