package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ICommandResult;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.Date;

import static com.axiell.ehub.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_LOAN_WITH_PRODUCT_ID;
import static com.axiell.ehub.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID;

class GetLoansCommand extends AbstractElib3Command<CommandData>{

    GetLoansCommand(IElibFacade elibFacade, IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Loan loan = getLoanWithProductId(data, contentProviderRecordId, contentProviderConsumer);
        if (loan == null)
            forward(PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, data);
        else {
            ContentProviderLoanMetadata metadata = makeContentProviderLoanMetadata(data, contentProviderConsumer, contentProviderRecordId, loan);
            data.setContentProviderLoanMetadata(metadata);
            forward(PATRON_HAS_LOAN_WITH_PRODUCT_ID, data);
        }
    }

    private Loan getLoanWithProductId(CommandData data, String contentProviderRecordId, ContentProviderConsumer contentProviderConsumer) {
        final Patron patron = data.getPatron();
        final GetLoansResponse getLoansResponse = elibFacade.getLoans(contentProviderConsumer, patron);
        return getLoansResponse.getLoanWithProductId(contentProviderRecordId);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(CommandData data, ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, Loan loan) {
        final Date expirationDate = loan.getExpirationDate();
        final String contentProviderLoanId = loan.getLoanId();
        final String formatId = data.getContentProviderFormatId();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        return new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
    }

    static enum Result implements ICommandResult {
        PATRON_HAS_LOAN_WITH_PRODUCT_ID, PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID
    }
}
