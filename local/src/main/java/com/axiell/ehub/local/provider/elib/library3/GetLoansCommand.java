package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.SupplementLinks;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ICommandResult;

import java.util.Date;
import java.util.List;

import static com.axiell.ehub.local.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_LOAN_WITH_PRODUCT_ID;
import static com.axiell.ehub.local.provider.elib.library3.GetLoansCommand.Result.PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID;

class GetLoansCommand extends AbstractElib3Command<CommandData> {

    GetLoansCommand(IElibFacade elibFacade, IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final LoanDTO loan = getLoanWithProductId(data, contentProviderConsumer);
        if (loan == null)
            forward(PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID, data);
        else {
            populateContentUrlInCommandData(data, loan);
            populateContentProviderLoanMetadataInCommandData(data, contentProviderConsumer, loan);
            forward(PATRON_HAS_LOAN_WITH_PRODUCT_ID, data);
        }
    }

    private LoanDTO getLoanWithProductId(final CommandData data, final ContentProviderConsumer contentProviderConsumer) {
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Patron patron = data.getPatron();
        final GetLoansResponse getLoansResponse = elibFacade.getLoans(contentProviderConsumer, patron);
        return getLoansResponse.getLoanWithProductId(contentProviderRecordId);
    }

    private void populateContentUrlInCommandData(final CommandData data, final LoanDTO loan) {
        final String formatId = data.getContentProviderFormatId();
        if (formatId != null) {
            final List<String> contentLinkHrefs = loan.getContentUrlsFor(formatId);
            data.setContentLinkHrefs(contentLinkHrefs);
        }
        final Supplements supplements = loan.getSupplements();
        if (supplements != null) {
            data.setSupplementLinks(new SupplementLinks(supplements.getSupplementLinks()));
        }
    }

    private void populateContentProviderLoanMetadataInCommandData(final CommandData data, final ContentProviderConsumer contentProviderConsumer,
                                                                  final LoanDTO loan) {
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Date expirationDate = loan.getExpirationDate();
        final String contentProviderLoanId = loan.getLoanId();
        final String formatId = data.getContentProviderFormatId();
        if (formatId != null) {
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
            final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, contentProviderRecordId,
                    formatDecoration).contentProviderLoanId(contentProviderLoanId).build();
            data.setContentProviderLoanMetadata(metadata);
        }
    }

    static enum Result implements ICommandResult {
        PATRON_HAS_LOAN_WITH_PRODUCT_ID, PATRON_HAS_NO_LOAN_WITH_PRODUCT_ID
    }
}
