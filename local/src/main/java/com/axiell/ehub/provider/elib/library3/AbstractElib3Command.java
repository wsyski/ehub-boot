package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.provider.AbstractCommand;
import com.axiell.ehub.provider.ICommandData;

abstract class AbstractElib3Command<D extends ICommandData> extends AbstractCommand<D> {
    protected final IElibFacade elibFacade;
    protected final IEhubExceptionFactory exceptionFactory;

    protected AbstractElib3Command(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        this.elibFacade = elibFacade;
        this.exceptionFactory = exceptionFactory;
    }
}