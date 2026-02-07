package com.axiell.ehub.lms;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.*;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.PendingLoan;

public class BaseLmsDataAccessor {
    protected static final String LMS_ERROR_MESSAGE = "Error in lms";

    protected InternalServerErrorException createCheckOutTestInternalErrorException(final EhubConsumer ehubConsumer, final String checkoutTestStatus) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, checkoutTestStatus);
        return new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
    }

    protected ForbiddenException createCheckOutTestCheckoutDeniedException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan,
                                                                         final Patron patron) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.lmsRecordId());
        final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, patron.getLibraryCard());
        final ErrorCauseArgument argLmsStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, LMS_ERROR_MESSAGE);
        return new ForbiddenException(ErrorCause.LMS_CHECKOUT_DENIED, argLmsRecordId, argLibraryCard, argEhubConsumerId, argLmsStatus);
    }

    protected NotFoundException createCheckOutTestNotFoundException(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.lmsRecordId());
        return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
    }

}
