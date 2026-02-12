package com.axiell.ehub.local.loan;

import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.ErrorCauseArgument.Type;
import com.axiell.ehub.common.InternalServerErrorException;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EhubLoanRepositoryFacade implements IEhubLoanRepositoryFacade {

    @Autowired
    private IEhubLoanRepository ehubLoanRepository;

    @Override
    public EhubLoan saveEhubLoan(final EhubConsumer ehubConsumer, final LmsLoan lmsLoan, final ContentProviderLoan contentProviderLoan) {
        final ContentProviderLoanMetadata contentProviderLoanMetadata = contentProviderLoan.getMetadata();
        final Long ehubConsumerId = ehubConsumer.getId();
        final String lmsLoanId = lmsLoan.getId();
        EhubLoan ehubLoan = ehubLoanRepository.findLoan(ehubConsumerId, lmsLoanId);
        if (ehubLoan != null) {
            final ErrorCauseArgument argument0 = new ErrorCauseArgument(Type.LMS_LOAN_ID, lmsLoanId);
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.EHUB_CONSUMER_ID, ehubConsumerId);
            throw new InternalServerErrorException(ErrorCause.LOAN_ALREADY_EXISTS, argument0, argument1);
        }
        ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
        return ehubLoanRepository.save(ehubLoan);
    }

    @Override
    public EhubLoan findEhubLoan(final EhubConsumer ehubConsumer, final String lmsLoanId) {
        final Long ehubConsumerId = ehubConsumer.getId();
        final EhubLoan ehubLoan = ehubLoanRepository.findLoan(ehubConsumerId, lmsLoanId);
        if (ehubLoan == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.LMS_LOAN_ID, lmsLoanId);
            throw new NotFoundException(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, argument);
        }
        return ehubLoan;
    }

    @Override
    public EhubLoan findEhubLoan(final long readyLoanId) {
        return ehubLoanRepository.findById(readyLoanId)
                .orElseThrow(() -> {
                    final ErrorCauseArgument argument = new ErrorCauseArgument(Type.READY_LOAN_ID, readyLoanId);
                    return new NotFoundException(ErrorCause.LOAN_BY_ID_NOT_FOUND, argument);
                });
    }

    @Override
    public long countLoansByFormatDecoration(final FormatDecoration formatDecoration) {
        if (formatDecoration == null)
            return 0;

        final Long formatDecorationId = formatDecoration.getId();
        return ehubLoanRepository.countLoansByFormatDecorationId(formatDecorationId);
    }

    @Override
    public void deleteByContentProviderId(final long contentProviderId) {
        ehubLoanRepository.deleteByContentProviderId(contentProviderId);
    }

}
