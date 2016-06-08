package com.axiell.ehub.support.request.loan;

import com.axiell.ehub.support.request.*;

class CreateLoanButton extends AbstractRequestGeneratorButton<DefaultSupportResponse> {

    CreateLoanButton(final String id, final IRequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected DefaultSupportResponse getResponse(final RequestArguments arguments) {
        return supportRequestAdminController.createLoan(arguments);
    }
}
