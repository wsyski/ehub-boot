package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.CreateContentCommand;
import com.axiell.ehub.provider.IContentFactory;

import static com.axiell.ehub.provider.elib.library3.BookAvailabilityCommand.Result.PRODUCT_AVAILABLE;
import static com.axiell.ehub.provider.elib.library3.CreateLoanCommand.Result.LOAN_CREATED;

class CreateLoanCommandChain extends AbstractElib3CommandChain<ContentProviderLoan, CommandData> {
    private final BookAvailabilityCommand firstCommand;
    private final CreateLoanCommand createLoanCommand;
    private final CreateContentCommand createContentCommand;

    CreateLoanCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory, final IContentFactory contentFactory) {
        super(elibFacade, exceptionFactory);
        firstCommand = new BookAvailabilityCommand(elibFacade, exceptionFactory);
        createLoanCommand = new CreateLoanCommand(elibFacade, exceptionFactory);
        createContentCommand = new CreateContentCommand(contentFactory);
        configureFirstCommand();
        configureCreateLoanCommand();
        configureCreateContentCommand();
    }

    private void configureFirstCommand() {
        firstCommand.on(BookAvailabilityCommand.Result.PRODUCT_AVAILABLE, createLoanCommand);
    }

    private void configureCreateLoanCommand() {
        createLoanCommand.on(CreateLoanCommand.Result.LOAN_CREATED, createContentCommand);
    }

    private void configureCreateContentCommand() {
        createContentCommand.next(END_COMMAND);
    }

    @Override
    public ContentProviderLoan execute(final CommandData data) {
        firstCommand.run(data);
        final ContentProviderLoanMetadata metadata = data.getContentProviderLoanMetadata();
        final IContent content = data.getContent();
        return new ContentProviderLoan(metadata, content);
    }
}
