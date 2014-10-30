package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class AbstractRequestGeneratorButton<R extends ISupportResponse> extends IndicatingAjaxButton {
    private final IRequestsGeneratorMediator mediator;

    @SpringBean(name = "supportRequestAdminController")
    protected ISupportRequestAdminController supportRequestAdminController;

    protected AbstractRequestGeneratorButton(final String id, final IRequestsGeneratorMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected final void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        final RequestArguments requestArguments = (RequestArguments) form.getModelObject();
        final ISupportResponse response = getResponse(requestArguments);
        mediator.afterResponseWasReceived(response, target);
    }

    protected abstract R getResponse(RequestArguments requestArguments);
}
