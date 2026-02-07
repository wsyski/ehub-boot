package com.axiell.ehub.lms.arena;

import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ForbiddenException;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.lms.BaseLmsDataAccessor;
import com.axiell.ehub.lms.CheckoutTestAnalysis;
import com.axiell.ehub.lms.ILmsDataAccessor;
import com.axiell.ehub.lms.arena.client.IArenaLocalService;
import com.axiell.ehub.lms.arena.error.ArenaLocalRestApiError;
import com.axiell.ehub.lms.arena.exception.RestApiException;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckOutTestSummary;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaRequestDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutEmediaResponseDTO;
import com.axiell.ehub.lms.arena.controller.patrons.dto.CheckoutTestEmediaResponseDTO;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.OpenEntityFieldCollection;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.RecordsDTO;
import com.axiell.ehub.lms.arena.controller.portalsites.dto.SortAdvice;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.util.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER;
import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_LOCAL_API_ENDPOINT;

/**
 * Arena Local Api implementation of the {@link ILmsDataAccessor}.
 */
@Component
public class ArenaLocalDataAccessor extends BaseLmsDataAccessor implements ILmsDataAccessor {
    private static final String MSG_ID_KEY = "msgId";
    @Autowired
    private IArenaLocalService localRestApiService;

    @Override
    public CheckoutTestAnalysis checkoutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron, final boolean isLoanPerProduct, final Locale locale) {
        String arenaLocalApiEndpoint = getArenaLocalApi(ehubConsumer);
        long arenaMemberId = getArenaMemberId(ehubConsumer);
        AuthInfo authinfo = new AuthInfo.Builder().ehubConsumerId(ehubConsumer.getId()).arenaAgencyMemberId(arenaMemberId).patron(patron).build();
        CheckoutTestEmediaResponseDTO checkoutTestEmediaResponse;
        try {
            checkoutTestEmediaResponse = localRestApiService.checkoutEmediaTest(arenaLocalApiEndpoint, locale, authinfo, pendingLoan.lmsRecordId(), pendingLoan.contentProviderAlias(), pendingLoan.contentProviderFormatId(), pendingLoan.issueId(), isLoanPerProduct);
        } catch (RestApiException ex) {
            throw handleRestApiException(ex, ehubConsumer, pendingLoan, patron);
        }
        return getCheckoutTestAnalysis(ehubConsumer, pendingLoan, patron, checkoutTestEmediaResponse);
    }


    @Override
    public LmsLoan checkout(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final Patron patron, final boolean isLoanPerProduct, final Locale locale) {
        String arenaLocalApiEndpoint = getArenaLocalApi(ehubConsumer);
        long arenaMemberId = getArenaMemberId(ehubConsumer);
        AuthInfo authinfo = new AuthInfo.Builder().ehubConsumerId(ehubConsumer.getId()).arenaAgencyMemberId(arenaMemberId).patron(patron).build();
        CheckoutEmediaRequestDTO checkoutEmediaRequest = new CheckoutEmediaRequestDTO(ConverterUtil.date2LocalDate(expirationDate), pendingLoan.issueId(), isLoanPerProduct);
        CheckoutEmediaResponseDTO checkoutEmediaResponse;
        try {
            checkoutEmediaResponse = localRestApiService.checkoutEmedia(arenaLocalApiEndpoint, locale, authinfo, pendingLoan.lmsRecordId(), pendingLoan.contentProviderAlias(), pendingLoan.contentProviderFormatId(), checkoutEmediaRequest);
        } catch (RestApiException ex) {
            throw handleRestApiException(ex, ehubConsumer, pendingLoan, patron);
        }
        return new LmsLoan(checkoutEmediaResponse.getLoanId());
    }

    @Override
    public String getMediaClass(final EhubConsumer ehubConsumer, final String contentProviderAlias, final String contentProviderRecordId, final Locale locale) {
        String arenaLocalApiEndpoint = getArenaLocalApi(ehubConsumer);
        long arenaMemberId = getArenaMemberId(ehubConsumer);
        String query = "contentProviderRecordId_index: \"" + contentProviderRecordId + "\" AND contentProviderName_index: \"" + contentProviderAlias + "\"";
        RecordsDTO records = localRestApiService.search(arenaLocalApiEndpoint, locale, arenaMemberId, query, 0, 1, Collections.singleton(OpenEntityFieldCollection.Basic), SortAdvice.Default.getDirection(), SortAdvice.Default.getField(), null, new HashSet<>());
        String mediaClass = null;
        if (records.getTotal() == 0) {
            throw new InternalServerErrorException("Missing record contentProviderAlias: " + contentProviderAlias + " contentProviderRecordId: " + contentProviderRecordId);
        } else {
            mediaClass = (String) records.getRecords().get(0).getFields().get("mediaClass");
            if (mediaClass == null) {
                throw new InternalServerErrorException("Missing mediaClass for record contentProviderAlias: " + contentProviderAlias + " contentProviderRecordId: " + contentProviderRecordId);
            }
            if (records.getTotal() > 1) {
                throw new InternalServerErrorException("Duplicate records for contentProviderAlias: " + contentProviderAlias + " contentProviderRecordId: " + contentProviderRecordId);
            }
        }
        return mediaClass;
    }


    private String getArenaLocalApi(final EhubConsumer ehubConsumer) {
        return ehubConsumer.getProperties().get(ARENA_LOCAL_API_ENDPOINT);
    }

    private long getArenaMemberId(final EhubConsumer ehubConsumer) {
        String arenaMemberIdAsString = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        long arenaMemberId;
        try {
            arenaMemberId = Long.parseLong(arenaMemberIdAsString);
        } catch (NumberFormatException ex) {
            throw new InternalServerErrorException("Non parseable agency member id: " + arenaMemberIdAsString);
        }
        return arenaMemberId;
    }

    private CheckoutTestAnalysis getCheckoutTestAnalysis(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron,
                                                         final CheckoutTestEmediaResponseDTO checkoutTestEmediaResponse) {
        String lmsLoanId = null;
        CheckoutTestAnalysis.Result result;
        CheckOutTestSummary.Status testStatus = checkoutTestEmediaResponse.getStatus();
        if (testStatus != null) {
            switch (testStatus) {
                case NEW_LOAN:
                    result = CheckoutTestAnalysis.Result.NEW_LOAN;
                    break;
                case ACTIVE_LOAN:
                    lmsLoanId = checkoutTestEmediaResponse.getLoanId();
                    result = CheckoutTestAnalysis.Result.ACTIVE_LOAN;
                    break;
                case CHECK_OUT_DENIED:
                    throw createCheckOutTestCheckoutDeniedException(ehubConsumer, pendingLoan, patron);
                case INVALID_RECORD_ID:
                    throw createCheckOutTestNotFoundException(ehubConsumer, pendingLoan);
                default:
                    throw createCheckOutTestInternalErrorException(ehubConsumer, checkoutTestEmediaResponse.getStatus().value());
            }
            return new CheckoutTestAnalysis(result, lmsLoanId);
        } else {
            throw new InternalServerErrorException("CheckOutTestResponse testStatus can not be null");
        }
    }

    private RuntimeException handleRestApiException(final RestApiException restApiException, final EhubConsumer ehubConsumer,
                                                    final PendingLoan pendingLoan, final Patron patron) {
        ArenaLocalRestApiError restApiError = restApiException.getRestApiError();
        final ErrorCauseArgument argEhubConsumerId = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumer.getId());
        final ErrorCauseArgument argLmsRecordId = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_RECORD_ID, pendingLoan.lmsRecordId());
        switch (restApiError.getCause()) {
            case STORE_ALMA_EXCEPTION:
                Map<String, String> arguments = restApiError.getArguments();
                String msgId = arguments.get(MSG_ID_KEY);
                final ErrorCauseArgument argLmsStatus = new ErrorCauseArgument(ErrorCauseArgument.Type.LMS_STATUS, msgId);
                return new ForbiddenException(ErrorCause.LMS_ERROR, argLmsStatus, argEhubConsumerId);
            case RECORD_NOT_FOUND_EXCEPTION:
                return new NotFoundException(ErrorCause.LMS_RECORD_NOT_FOUND, argLmsRecordId, argEhubConsumerId);
            default:
                return new InternalServerErrorException(restApiError.getCause().getMessage(restApiError.getArguments()));
        }
    }
}
