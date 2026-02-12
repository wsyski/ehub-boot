package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

import java.util.Collection;

public class PlatformsChoice extends ListMultipleChoice<Platform> {

    public PlatformsChoice(final String id, final IModel<Collection<Platform>> platformsModel) {
        super(id);
        setChoices();
        setChoiceRenderer();
        setOutputMarkupId(true);
        setModel(platformsModel);
    }

    private void setChoiceRenderer() {
        final PlatformsChoiceRenderer choiceRenderer = new PlatformsChoiceRenderer();
        setChoiceRenderer(choiceRenderer);
    }

    private void setChoices() {
        final PlatformsChoiceModel platformsChoiceModel = new PlatformsChoiceModel();
        setChoices(platformsChoiceModel);
    }
}
