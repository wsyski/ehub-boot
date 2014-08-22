package com.axiell.ehub.support.request.format;

import com.axiell.ehub.support.request.AbstractRequestGeneratorButton;
import com.axiell.ehub.support.request.RequestArguments;
import com.axiell.ehub.support.request.RequestsGeneratorMediator;
import com.axiell.ehub.support.request.SupportResponse;

class GetFormatsButton extends AbstractRequestGeneratorButton {

    GetFormatsButton(final String id, final RequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected SupportResponse getResponse(final RequestArguments arguments) {
        return supportRequestAdminController.getFormats(arguments);
    }
}
