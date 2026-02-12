package com.axiell.ehub.local.provider.record.format;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

class ContentDispositionChoiceOnChangeBehavior extends AjaxFormComponentUpdatingBehavior {
    private final IContentDispositionChangedAwareMediator mediator;

    ContentDispositionChoiceOnChangeBehavior(final IContentDispositionChangedAwareMediator mediator) {
        super("onchange");
        this.mediator = mediator;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        mediator.afterContentDispositionChanged(target);
    }
}
