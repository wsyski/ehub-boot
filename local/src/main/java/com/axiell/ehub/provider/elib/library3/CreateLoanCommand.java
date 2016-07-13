package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.SupplementLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ICommandResult;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.Date;
import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentType.MISSING_CONTENT_IN_LOAN;
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
        final List<String> contentLinkHrefs = createdLoan.getContentUrlsFor(formatId);

        if (contentLinkHrefs == null || contentLinkHrefs.isEmpty())
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN,
                    language);
        else {
            data.setContentLinkHrefs(contentLinkHrefs);
            final Supplements supplements = createdLoan.getSupplements();
            if (supplements != null) {
                data.setSupplementLinks(new SupplementLinks(supplements.getSupplementLinks()));
            }
            populateContentProviderLoanMetadataInCommandData(data, contentProviderConsumer, contentProviderRecordId, formatId, createdLoan);
            forward(LOAN_CREATED, data);
        }
    }

    private void populateContentProviderLoanMetadataInCommandData(CommandData data, ContentProviderConsumer contentProviderConsumer,
                                                                  String contentProviderRecordId, String formatId, CreatedLoan createdLoan) {
        final Date expirationDate = createdLoan.getExpirationDate();
        final String contentProviderLoanId = createdLoan.getLoanId();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final ContentProviderLoanMetadata metadata =
                new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId, formatDecoration)
                        .contentProviderLoanId(contentProviderLoanId).build();
        data.setContentProviderLoanMetadata(metadata);
    }

    public enum Result implements ICommandResult {
        LOAN_CREATED
    }
}
