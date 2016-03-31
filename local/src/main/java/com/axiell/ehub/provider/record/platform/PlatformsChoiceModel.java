package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

class PlatformsChoiceModel extends LoadableDetachableModel<List<Platform>> {

    @SpringBean(name = "remoteSelfPubClient")
    private IPlatformAdminController platformAdminController;

    PlatformsChoiceModel() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected List<Platform> load() {
        return platformAdminController.findAll();
    }
}
