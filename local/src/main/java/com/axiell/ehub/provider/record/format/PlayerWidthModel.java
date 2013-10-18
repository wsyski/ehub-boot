package com.axiell.ehub.provider.record.format;

import org.apache.wicket.model.IModel;

class PlayerWidthModel extends AbstractPlayerModel {
    
    PlayerWidthModel(final IModel<FormatDecoration> formModel) {
	super(formModel);
    }

    @Override
    protected Integer getObject(final FormatDecoration formatDecoration) {
	return formatDecoration.getPlayerWidth();
    }

    @Override
    protected void setObject(final FormatDecoration formatDecoration, final Integer value) {
	formatDecoration.setPlayerWidth(value);
    }
}
