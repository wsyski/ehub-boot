package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ICommandResult;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.INACTIVE_LOAN;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.MISSING_CONTENT_IN_LOAN;
import static com.axiell.ehub.provider.elib.library3.GetLoanCommand.Result.ACTIVE_LOAN_RETRIEVED;

class GetLoanCommand extends AbstractElib3Command<CommandData> {

    GetLoanCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final String elibLoanId = contentProviderLoanMetadata.getId();
        final String langauge = data.getLanguage();
        final Loan loan = elibFacade.getLoan(contentProviderConsumer, elibLoanId);

        if (loan.isActive()) {
            final String contentUrl = loan.getFirstContentUrl();
            if (contentUrl == null)
                throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN, langauge);
            else {
                final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
                data.setContentUrl(contentUrl);
                data.setFormatDecoration(formatDecoration);
                forward(ACTIVE_LOAN_RETRIEVED, data);
            }
        } else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INACTIVE_LOAN, langauge);
    }

    public static enum Result implements ICommandResult {
        ACTIVE_LOAN_RETRIEVED
    }
}
