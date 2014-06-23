package com.axiell.ehub.support.request;

import static com.axiell.ehub.support.request.SupportRequestExecutor.getFormats;

class GetFormatsButton extends AbstractRequestGeneratorButton {

    GetFormatsButton(final String id, final RequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected SupportResponse getResponse(final RequestArguments arguments) {
        return getFormats(arguments);
    }
}
