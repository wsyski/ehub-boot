package com.axiell.ehub.support.request;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;

abstract class AbstractRequestGeneratorButton extends IndicatingAjaxButton {
    private final RequestsGeneratorMediator mediator;

    AbstractRequestGeneratorButton(final String id, final RequestsGeneratorMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected final void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
        final RequestArguments requestArguments = (RequestArguments) form.getModelObject();
        final SupportResponse response = getResponse(requestArguments);
        mediator.afterResponseWasReceived(response, target);
    }

    protected abstract SupportResponse getResponse(RequestArguments requestArguments);
}
