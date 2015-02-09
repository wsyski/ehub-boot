package com.axiell.ehub.loan;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;

public class EhubLoanRepositoryFacade implements IEhubLoanRepositoryFacade {
    @Autowired(required = true)
    private IEhubLoanRepository ehubLoanRepository;

    @Override
    public EhubLoan saveEhubLoan(final EhubConsumer ehubConsumer, final LmsLoan lmsLoan, final ContentProviderLoan contentProviderLoan) {
        final ContentProviderLoanMetadata contentProviderLoanMetadata = contentProviderLoan.getMetadata();
        final EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        return ehubLoanRepository.save(ehubLoan);
    }

    @Override
    public EhubLoan findEhubLoan(final EhubConsumer ehubConsumer, final String lmsLoanId) {
        final Long ehubConsumerId = ehubConsumer.getId();
        return ehubLoanRepository.findLoan(ehubConsumerId, lmsLoanId);
    }

    @Override
    public EhubLoan findEhubLoan(final EhubConsumer ehubConsumer, final Long readyLoanId) {
        final Long ehubConsumerId = ehubConsumer.getId();
        final EhubLoan ehubLoan = ehubLoanRepository.findLoan(ehubConsumerId, readyLoanId);

        if (ehubLoan == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.READY_LOAN_ID, readyLoanId);
            throw new NotFoundException(ErrorCause.LOAN_BY_ID_NOT_FOUND, argument);
        }

        return ehubLoan;
    }

    @Override
    public long countLoansByFormatDecoration(final FormatDecoration formatDecoration) {
        if (formatDecoration == null)
            return 0;

        final Long formatDecorationId = formatDecoration.getId();
        return ehubLoanRepository.countLoansByFormatDecorationId(formatDecorationId);
    }
}