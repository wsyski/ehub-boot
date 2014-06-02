package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.CreateContentCommand;
import com.axiell.ehub.provider.IContentFactory;

import static com.axiell.ehub.provider.elib.library3.GetLoanCommand.Result.ACTIVE_LOAN_RETRIEVED;

class GetLoanCommandChain extends AbstractElib3CommandChain<IContent, CommandData> {
    private final GetLoanCommand firstCommand;
    private final CreateContentCommand createContentCommand;

    GetLoanCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory, final IContentFactory contentFactory) {
        super(elibFacade, exceptionFactory);
        firstCommand = new GetLoanCommand(elibFacade, exceptionFactory);
        createContentCommand = new CreateContentCommand(contentFactory);
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
    public IContent execute(final CommandData data) {
        firstCommand.run(data);
        return data.getContent();
    }
}
