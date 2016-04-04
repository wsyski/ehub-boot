package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

class PlatformModel extends LoadableDetachableModel<Platform> {
    private Long id;

    @SpringBean(name = "platformAdminController")
    private IPlatformAdminController platformAdminController;

    PlatformModel(final Long id) {
        InjectorHolder.getInjector().inject(this);
        this.id = id;
    }

    PlatformModel(final Platform platform) {
        this(platform.getId());
    }

    @Override
    protected Platform load() {
        return id == null ? new Platform() : platformAdminController.getPlatform(id);
    }
}
