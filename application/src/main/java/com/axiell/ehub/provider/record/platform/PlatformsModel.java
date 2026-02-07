package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.apache.wicket.model.IModel;

import java.util.Collection;
import java.util.HashSet;

public class PlatformsModel implements IModel<Collection<Platform>> {
    private final IModel<FormatDecoration> formModel;

    public PlatformsModel(final IModel<FormatDecoration> formModel) {
        this.formModel = formModel;
    }

    @Override
    public Collection<Platform> getObject() {
        FormatDecoration formatDecoration = formModel.getObject();
        return formatDecoration.getPlatforms();
    }

    @Override
    public void setObject(Collection<Platform> platforms) {
        FormatDecoration formatDecoration = formModel.getObject();
        formatDecoration.setPlatforms(new HashSet<>(platforms));
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
