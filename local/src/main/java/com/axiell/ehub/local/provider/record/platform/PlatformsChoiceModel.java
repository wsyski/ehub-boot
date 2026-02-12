package com.axiell.ehub.local.provider.record.platform;

import com.axiell.ehub.common.provider.platform.Platform;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

class PlatformsChoiceModel extends LoadableDetachableModel<List<Platform>> {

    @SpringBean(name = "platformAdminController")
    private IPlatformAdminController platformAdminController;

    PlatformsChoiceModel() {
        Injector.get().inject(this);
    }

    @Override
    protected List<Platform> load() {
        return platformAdminController.findAll();
    }
}
