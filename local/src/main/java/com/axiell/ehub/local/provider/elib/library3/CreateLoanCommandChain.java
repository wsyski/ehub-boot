package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.CreateContentCommand;

class CreateLoanCommandChain extends AbstractElib3CommandChain<ContentProviderLoan, CommandData> {
    private final GetLoansCommand firstCommand;
    private final BookAvailabilityCommand bookAvailabilityCommand;
    private final CreateLoanCommand createLoanCommand;
    private final CreateContentCommand createContentCommand;

    CreateLoanCommandChain(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
        firstCommand = new GetLoansCommand(elibFacade, exceptionFactory);
        bookAvailabilityCommand = new BookAvailabilityCommand(elibFacade, exceptionFactory);
        createLoanCommand = new CreateLoanCommand(elibFacade, exceptionFactory);
        createContentCommand = new CreateContentCommand();
        configureFirstCommand();
        configureBookAvailabilityCommand();
        configureCreateLoanCommand();
        configureCreateContentCommand();
    }

    private void configureFirstCommand() {
        firstCommand.on(GetLoansCommand.Result.PATRON_HAS_LOAN_WITH_PRODUCT_ID, createContentCommand);
        firstCommand.on(GetLoansCommand.Result.PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, bookAvailabilityCommand);
    }

    private void configureBookAvailabilityCommand() {
        bookAvailabilityCommand.on(BookAvailabilityCommand.Result.PRODUCT_AVAILABLE, createLoanCommand);
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
        final Content content = data.getContent();
        return new ContentProviderLoan(metadata, content);
    }
}
