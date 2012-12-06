/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.lms.palma.PreCheckoutAnalysis;
import com.axiell.ehub.lms.palma.PreCheckoutAnalysis.Result;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.security.AuthInfo;

/**
 * Default implementation of the {@link ILoanBusinessController}.
 */
public class LoanBusinessController implements ILoanBusinessController {
    @Autowired(required = true)
    private IConsumerBusinessController consumerBusinessController; 

    @Autowired(required = true)
    private IPalmaDataAccessor palmaDataAccessor;
    
    @Autowired(required = true)
    private ElibDataAccessor elibDataAccessor;
    
    @Autowired(required = true)
    private ElibUDataAccessor elibUDataAccessor;
        
    @Autowired(required = true)
    private IEhubLoanRepository ehubLoanRepository;
    
    /**
     * @see com.axiell.ehub.loan.ILoanBusinessController#createLoan(com.axiell.ehub.security.AuthInfo, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    @Transactional(readOnly = false)
    public ReadyLoan createLoan(AuthInfo authInfo, PendingLoan pendingLoan) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        final PreCheckoutAnalysis preCheckoutAnalysis = palmaDataAccessor.preCheckout(ehubConsumer, pendingLoan, libraryCard, pin);
        final Result result = preCheckoutAnalysis.getResult();

        switch (result) {
            case NEW_LOAN:
                final ContentProviderName contentProviderName = pendingLoan.getContentProviderNameEnum();
                final ContentProviderConsumer contentProviderConsumer = ehubConsumer.getContentProviderConsumer(contentProviderName);
                final ContentProviderLoan contentProviderLoan;
                switch (contentProviderName) {
                    case ELIB:
                        contentProviderLoan = elibDataAccessor.createLoan(contentProviderConsumer, libraryCard, pin, pendingLoan);
                        break;
                    case ELIBU:
                        contentProviderLoan = elibUDataAccessor.createLoan(contentProviderConsumer, libraryCard, pin, pendingLoan);
                        break;
                    default:
                        throw new NotImplementedException("Create new loan for content provider with name '" + contentProviderName + "' has not been implemented");
                }

                final ContentProviderLoanMetadata contentProviderLoanMetadata = contentProviderLoan.getMetadata();
                final LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, contentProviderLoan.getExpirationDate(), libraryCard, pin);
                EhubLoan ehubLoan = new EhubLoan(ehubConsumer, lmsLoan, contentProviderLoanMetadata);
                ehubLoan = ehubLoanRepository.save(ehubLoan);
                final Long readyLoanId = ehubLoan.getId();
                return new ReadyLoan(readyLoanId, lmsLoan, contentProviderLoan);
            case ACTIVE_LOAN:
                final String lmsLoanId = preCheckoutAnalysis.getLmsLoanId();
                return getReadyLoan(authInfo, lmsLoanId);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    /**
     * @see com.axiell.ehub.loan.ILoanBusinessController#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public ReadyLoan getReadyLoan(AuthInfo authInfo, Long readyLoanId) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        final EhubLoan ehubLoan = ehubLoanRepository.findOne(readyLoanId);
        
        if (ehubLoan == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.READY_LOAN_ID, readyLoanId);
            throw new NotFoundException(ErrorCause.LOAN_BY_ID_NOT_FOUND, argument);
        } else {
            return getReadyLoan(ehubConsumer, libraryCard, pin, ehubLoan);    
        }
    }

    /**
     * @see com.axiell.ehub.loan.ILoanBusinessController#getReadyLoan(com.axiell.ehub.security.AuthInfo, java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public ReadyLoan getReadyLoan(AuthInfo authInfo, String lmsLoanId) {
        final Long ehubConsumerId = authInfo.getEhubConsumerId();
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        final EhubLoan ehubLoan = ehubLoanRepository.getLoan(ehubConsumerId, lmsLoanId);
        
        if (ehubLoan == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.LMS_LOAN_ID, lmsLoanId);
            throw new NotFoundException(ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND, argument);
        } else {
            return getReadyLoan(ehubConsumer, libraryCard, pin, ehubLoan);    
        }
    }
    
    /**
     * Gets a {@link ReadyLoan}.
     * 
     * @param ehubConsumer the {@link EhubConsumer}
     * @param libraryCard the library card
     * @param pin the pin
     * @param ehubLoan the retrieved {@link EhubLoan}
     * @return a {@link ReadyLoan}
     */
    private ReadyLoan getReadyLoan(final EhubConsumer ehubConsumer, final String libraryCard, final String pin, final EhubLoan ehubLoan) {
        final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
        final ContentProvider contentProvider = contentProviderLoanMetadata.getContentProvider();
        final ContentProviderName contentProviderName = contentProvider.getName();
        final ContentProviderConsumer contentProviderConsumer = ehubConsumer.getContentProviderConsumer(contentProviderName);
        final IContent content;

        switch (contentProviderName) {
            case ELIB:
                content = elibDataAccessor.getContent(contentProviderConsumer, libraryCard, pin, contentProviderLoanMetadata);
                break;
            case ELIBU:
                content = elibUDataAccessor.getContent(contentProviderConsumer, libraryCard, pin, contentProviderLoanMetadata);
                break;
            default:
                throw new NotImplementedException("Get ready loan for content provider with name '" + contentProviderName + "' has not been implemented");
        }

        final Long readyLoanId = ehubLoan.getId();
        final LmsLoan lmsLoan = ehubLoan.getLmsLoan();
        final ContentProviderLoan contentProviderLoan = new ContentProviderLoan(contentProviderLoanMetadata, content);
        return new ReadyLoan(readyLoanId, lmsLoan, contentProviderLoan);
    }
}
