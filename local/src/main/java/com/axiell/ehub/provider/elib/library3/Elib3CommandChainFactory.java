package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.IContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class Elib3CommandChainFactory implements IElib3CommandChainFactory {

    @Autowired(required = true)
    private IElibFacade elibFacade;

    @Autowired(required = true)
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired(required = true)
    protected IContentFactory contentFactory;

    @Override
    public GetFormatsCommandChain createGetFormatsCommandChain() {
        return new GetFormatsCommandChain(elibFacade, ehubExceptionFactory);
    }

    @Override
    public CreateLoanCommandChain createCreateLoanCommandChain() {
        return new CreateLoanCommandChain(elibFacade, ehubExceptionFactory, contentFactory);
    }

    @Override
    public GetContentCommandChain createGetContentCommandChain() {
        return new GetContentCommandChain(elibFacade, ehubExceptionFactory, contentFactory);
    }
}
