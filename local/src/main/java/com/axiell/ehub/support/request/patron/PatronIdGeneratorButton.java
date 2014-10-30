package com.axiell.ehub.support.request.patron;

import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.support.request.*;

class PatronIdGeneratorButton extends AbstractRequestGeneratorButton<GeneratedPatronResponse> {

    PatronIdGeneratorButton(final String id, final IRequestsGeneratorMediator mediator) {
        super(id, mediator);
    }

    @Override
    protected GeneratedPatronResponse getResponse(final RequestArguments arguments) {
        return new GeneratedPatronResponse(arguments);
    }
}
