package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.local.provider.record.platform.PlatformsChoice;
import com.axiell.ehub.local.provider.record.platform.PlatformsModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class PlayerContainer extends WebMarkupContainer {
    private final IModel<FormatDecoration> formModel;

    PlayerContainer(final String id, final IModel<FormatDecoration> formModel) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.formModel = formModel;
        addPlatformsField();
    }

    private void addPlatformsField() {
        final PlatformsChoice platformsChoice = new PlatformsChoice("platformsChoice", new PlatformsModel(formModel));
        add(platformsChoice);
    }

    /*
    @Override
    public boolean isVisible() {
        final FormatDecoration formatDecoration = formModel.getObject();
        final ContentDisposition contentDisposition = formatDecoration.getContentDisposition();
        return ContentDisposition.STREAMING.equals(contentDisposition);
    }
    */
}
