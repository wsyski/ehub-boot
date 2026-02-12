package com.axiell.ehub.local.provider.record.format;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface IContentDispositionChangedAwareMediator {

    void afterContentDispositionChanged(AjaxRequestTarget target);

    void registerPlayerContainer(PlayerContainer playerContainer);
}
