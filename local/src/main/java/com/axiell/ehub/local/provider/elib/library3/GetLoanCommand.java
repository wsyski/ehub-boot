package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.checkout.SupplementLinks;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ICommandResult;

import java.util.List;

import static com.axiell.ehub.common.ErrorCauseArgumentType.INACTIVE_LOAN;
import static com.axiell.ehub.common.ErrorCauseArgumentType.MISSING_CONTENT_IN_LOAN;
import static com.axiell.ehub.local.provider.elib.library3.GetLoanCommand.Result.ACTIVE_LOAN_RETRIEVED;

class GetLoanCommand extends AbstractElib3Command<CommandData> {

    GetLoanCommand(final IElibFacade elibFacade, final IEhubExceptionFactory exceptionFactory) {
        super(elibFacade, exceptionFactory);
    }

    @Override
    public void run(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final String elibLoanId = contentProviderLoanMetadata.getId();
        final String formatId = formatDecoration.getContentProviderFormatId();
        final String language = data.getLanguage();
        final LoanDTO loan = elibFacade.getLoan(contentProviderConsumer, elibLoanId);

        if (loan.isActive()) {
            final List<String> contentLinkHrefs = loan.getContentUrlsFor(formatId);
            if (contentLinkHrefs == null)
                throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, MISSING_CONTENT_IN_LOAN,
                        language);
            else {
                data.setContentLinkHrefs(contentLinkHrefs);
                final Supplements supplements = loan.getSupplements();
                if (supplements != null) {
                    data.setSupplementLinks(new SupplementLinks(supplements.getSupplementLinks()));
                }
                forward(ACTIVE_LOAN_RETRIEVED, data);
            }
        } else
            throw exceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, INACTIVE_LOAN, language);
    }

    public static enum Result implements ICommandResult {
        ACTIVE_LOAN_RETRIEVED
    }
}
