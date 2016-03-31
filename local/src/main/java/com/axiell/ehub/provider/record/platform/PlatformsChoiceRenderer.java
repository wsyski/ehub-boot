package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class PlatformsChoiceRenderer extends ChoiceRenderer<Platform> {


    @Override
    public String getIdValue(final Platform platform, int index) {
        return platform.getId() == null ? null : String.valueOf(platform.getId());
    }

    @Override
    public Object getDisplayValue(final Platform platform) {
        return platform.getName();
    }
}
