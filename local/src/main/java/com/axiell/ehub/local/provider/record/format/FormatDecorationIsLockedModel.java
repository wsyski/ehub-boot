package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.apache.wicket.model.IModel;

/**
 * The {@link IModel} for the description of an {@link FormatDecoration}.
 */
class FormatDecorationIsLockedModel implements IModel<Boolean> {
    private final IModel<FormatDecoration> formModel;

    FormatDecorationIsLockedModel(final IModel<FormatDecoration> formModel) {
        this.formModel = formModel;
    }

    @Override
    public Boolean getObject() {
        final FormatDecoration formatDecoration = formModel.getObject();
        return formatDecoration != null && formatDecoration.isLocked();
    }

    @Override
    public void setObject(final Boolean isLocked) {
        final FormatDecoration formatDecoration = formModel.getObject();
        formatDecoration.setLocked(isLocked);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
