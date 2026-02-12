package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

class PlatformsChoiceRenderer extends ChoiceRenderer<Platform> {


    @Override
    public String getIdValue(final Platform platform, int index) {
        return platform.getId() == null ? null : platform.getId().toString();
    }

    @Override
    public Object getDisplayValue(final Platform platform) {
        return platform.getName();
    }
}
