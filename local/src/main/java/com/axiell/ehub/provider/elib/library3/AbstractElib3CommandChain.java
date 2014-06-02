package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.provider.EndCommand;
import com.axiell.ehub.provider.ICommandChain;
import com.axiell.ehub.provider.ICommandData;

abstract class AbstractElib3CommandChain<T, D extends ICommandData> implements ICommandChain<T, D> {
    protected static final EndCommand END_COMMAND = new EndCommand();
    protected final IElibFacade elibFacade;
    protected final IEhubExceptionFactory exceptionFactory;

    protected AbstractElib3CommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        this.elibFacade = elibFacade;
        this.exceptionFactory = exceptionFactory;
    }
}
