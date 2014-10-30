package com.axiell.ehub.support.request.format;

import com.axiell.ehub.support.request.*;

class GetFormatsButton extends AbstractRequestGeneratorButton<DefaultSupportResponse> {

    GetFormatsButton(final String id, final IRequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected DefaultSupportResponse getResponse(final RequestArguments arguments) {
        return supportRequestAdminController.getFormats(arguments);
    }
}
