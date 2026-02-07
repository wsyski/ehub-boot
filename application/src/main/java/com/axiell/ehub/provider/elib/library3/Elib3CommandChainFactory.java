package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class Elib3CommandChainFactory implements IElib3CommandChainFactory {

    @Autowired
    private IElibFacade elibFacade;

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public GetFormatsCommandChain createGetFormatsCommandChain() {
        return new GetFormatsCommandChain(elibFacade, ehubExceptionFactory, formatFactory);
    }

    @Override
    public CreateLoanCommandChain createCreateLoanCommandChain() {
        return new CreateLoanCommandChain(elibFacade, ehubExceptionFactory);
    }

    @Override
    public GetContentCommandChain createGetContentCommandChain() {
        return new GetContentCommandChain(elibFacade, ehubExceptionFactory);
    }
}
