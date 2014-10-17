package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.util.status.Status;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.Validate;
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
    public void checkResponseStatus(Status status, EhubConsumer ehubConsumer, Patron patron) {
        String statusType = status.getType();
        if (!STATUS_OK.equals(statusType))
            throwException(status.getMessage(), ehubConsumer, patron);
    }

    private void throwException(String statusMessage, EhubConsumer ehubConsumer, Patron patron) {
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

    @Override
    public void check267ResponseStatus(com.axiell.arena.services.palma.util.v267.status.Status status, EhubConsumer ehubConsumer, Patron.Builder patronBuilder) {
        String statusType = status.getType();
        if (!STATUS_OK.equals(statusType))
            throwException(status.getMessage(), ehubConsumer, patronBuilder.build());
    }
}
