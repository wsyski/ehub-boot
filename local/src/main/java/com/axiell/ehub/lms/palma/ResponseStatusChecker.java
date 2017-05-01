package com.axiell.ehub.lms.palma;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.auth.Patron;
import org.springframework.stereotype.Component;

@Component
class ResponseStatusChecker implements IResponseStatusChecker {
    private static final String STATUS_OK = "ok";
    private static final String MESSAGE_BLOCKED_BORR_CARD = "BlockedBorrCard";
    private static final String MESSAGE_BORR_CARD_NOT_FOUND = "BorrCardNotFound";
    private static final String MESSAGE_INVALID_ACCOUNT_CARD = "InvalidAccountCard";
    private static final String MESSAGE_INVALID_BORR_CARD = "InvalidBorrCard";
    private static final String MESSAGE_INVALID_PATRON = "InvalidPatron";
    private static final String MESSAGE_INVALID_PIN_CODE = "InvalidPinCode";
    private static final String LMS_ERROR_MESSAGE = "Error in lms";

    @Override
    public void checkResponseStatus(final com.axiell.arena.services.palma.util.status.Status status, final EhubConsumer ehubConsumer, final Patron patron) {
        String statusType = status.getType();
        if (!STATUS_OK.equals(statusType))
            throwException(status.getMessage(), ehubConsumer, patron);
    }

    @Override
    public void checkResponseStatus(final com.axiell.arena.services.palma.util.v267.status.Status status, final EhubConsumer ehubConsumer) {
        String statusType = status.getType();
        if (!STATUS_OK.equals(statusType))
            throwException(status.getMessage(), ehubConsumer, null);
    }

    private void throwException(final String statusMessage, final EhubConsumer ehubConsumer, final Patron patron) {
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLibraryCard = new ErrorCauseArgument(ErrorCauseArgument.Type.LIBRARY_CARD, patron.getLibraryCard());
        if (statusMessage != null) {
            switch (statusMessage) {
                case MESSAGE_INVALID_PIN_CODE:
                    throw new ForbiddenException(ErrorCause.LMS_INVALID_PIN_CODE, argLibraryCard, argEhubConsumerId);
                case MESSAGE_BLOCKED_BORR_CARD:
                    throw new ForbiddenException(ErrorCause.LMS_BLOCKED_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
                case MESSAGE_BORR_CARD_NOT_FOUND:
                    throw new ForbiddenException(ErrorCause.LMS_LIBRARY_CARD_NOT_FOUND, argLibraryCard, argEhubConsumerId);
                case MESSAGE_INVALID_ACCOUNT_CARD:
                case MESSAGE_INVALID_BORR_CARD:
                case MESSAGE_INVALID_PATRON:
                    throw new ForbiddenException(ErrorCause.LMS_INVALID_LIBRARY_CARD, argLibraryCard, argEhubConsumerId);
            }
        }
        final ErrorCauseArgument argStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, statusMessage);
        throw new InternalServerErrorException(LMS_ERROR_MESSAGE, ErrorCause.LMS_ERROR, argStatus, argEhubConsumerId);
    }
}
