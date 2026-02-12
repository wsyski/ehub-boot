package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.AbstractCommand;
import com.axiell.ehub.local.provider.ICommandData;

abstract class AbstractElib3Command<D extends ICommandData> extends AbstractCommand<D> {
    protected final IElibFacade elibFacade;
    protected final IEhubExceptionFactory exceptionFactory;

    protected AbstractElib3Command(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        this.elibFacade = elibFacade;
        this.exceptionFactory = exceptionFactory;
    }
}
