package com.axiell.ehub.support;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;

abstract class AbstractRequestGeneratorButton extends IndicatingAjaxButton{
    private final RequestsGeneratorMediator mediator;

    AbstractRequestGeneratorButton(final String id, final RequestsGeneratorMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected final void onSubmit(AjaxRequestTarget target, Form<?> form) {
        final RequestParameters requestParameters = (RequestParameters) form.getModelObject();
        final SupportRequestExecutor executor = new SupportRequestExecutor();
        final SupportResponse response = getResponse(executor, requestParameters);
        mediator.afterResponseWasGenerated(response, target);
    }

    protected abstract SupportResponse getResponse(SupportRequestExecutor executor, RequestParameters requestParameters);
}
