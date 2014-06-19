package com.axiell.ehub.support;

class GetFormatsButton extends AbstractRequestGeneratorButton {

    GetFormatsButton(final String id, final RequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected SupportResponse getResponse(SupportRequestExecutor executor, RequestParameters request) {
        return executor.getFormats(request);
    }
}
