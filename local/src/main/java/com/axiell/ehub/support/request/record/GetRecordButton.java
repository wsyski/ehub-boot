package com.axiell.ehub.support.request.record;

import com.axiell.ehub.support.request.AbstractRequestGeneratorButton;
import com.axiell.ehub.support.request.DefaultSupportResponse;
import com.axiell.ehub.support.request.IRequestsGeneratorMediator;
import com.axiell.ehub.support.request.RequestArguments;

class GetRecordButton extends AbstractRequestGeneratorButton<DefaultSupportResponse> {

    GetRecordButton(final String id, final IRequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected DefaultSupportResponse getResponse(final RequestArguments arguments) {
        return supportRequestAdminController.getRecord(arguments);
    }
}
