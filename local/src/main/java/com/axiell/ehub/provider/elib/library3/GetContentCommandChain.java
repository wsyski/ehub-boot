package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.CreateContentCommand;
import com.axiell.ehub.provider.IContentFactory;

import static com.axiell.ehub.provider.elib.library3.GetLoanCommand.Result.ACTIVE_LOAN_RETRIEVED;

class GetContentCommandChain extends AbstractElib3CommandChain<ContentLink, CommandData> {
    private final GetLoanCommand firstCommand;
    private final CreateContentCommand createContentCommand;

    GetContentCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory, final IContentFactory contentFactory) {
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
    public ContentLink execute(final CommandData data) {
        firstCommand.run(data);
        return data.getContent();
    }
}
