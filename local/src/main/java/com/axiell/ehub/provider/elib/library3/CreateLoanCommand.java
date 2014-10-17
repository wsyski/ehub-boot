package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ICommandResult;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.Date;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type.MISSING_CONTENT_IN_LOAN;
import static com.axiell.ehub.provider.elib.library3.CreateLoanCommand.Result.LOAN_CREATED;

class CreateLoanCommand extends AbstractElib3Command<CommandData> {

    CreateLoanCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Patron patron = data.getPatron();
        final String language = data.getLanguage();
        final String formatId = data.getContentProviderFormatId();
        final CreatedLoan createdLoan = elibFacade.createLoan(contentProviderConsumer, contentProviderRecordId, patron);
        final String contentUrl = createdLoan.getContentUrlFor(formatId);

        if (contentUrl == null)
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN, language);
        else {
            final Date expirationDate = createdLoan.getExpirationDate();
            final String contentProviderLoanId = createdLoan.getLoanId();
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
            final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
            data.setContentUrl(contentUrl);
            data.setFormatDecoration(formatDecoration);
            data.setContentProviderLoanMetadata(metadata);
            forward(LOAN_CREATED, data);
        }
    }

    public static enum Result implements ICommandResult {
        LOAN_CREATED
    }
}
