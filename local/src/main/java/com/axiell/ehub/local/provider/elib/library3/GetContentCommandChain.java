package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.CreateContentCommand;

import static com.axiell.ehub.local.provider.elib.library3.GetLoanCommand.Result.ACTIVE_LOAN_RETRIEVED;

class GetContentCommandChain extends AbstractElib3CommandChain<Content, CommandData> {
    private final GetLoanCommand firstCommand;
    private final CreateContentCommand createContentCommand;

    GetContentCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
        firstCommand = new GetLoanCommand(elibFacade, exceptionFactory);
        createContentCommand = new CreateContentCommand();
        configureFirstCommand();
        configureCreateContentCommand();
    }

    private void configureFirstCommand() {
        firstCommand.on(ACTIVE_LOAN_RETRIEVED, createContentCommand);
    }

    private void configureCreateContentCommand() {
        createContentCommand.next(END_COMMAND);
    }

    @Override
    public Content execute(final CommandData data) {
        firstCommand.run(data);
        return data.getContent();
    }
}
