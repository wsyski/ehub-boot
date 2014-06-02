/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
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
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;

    @Autowired(required = true)
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;

    @Autowired(required = true)
    private IReadyLoanFactory readyLoanFactory;

    @Override
    @Transactional(readOnly = false)
    public ReadyLoan createLoan(final AuthInfo authInfo, final PendingLoan pendingLoan, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        final CheckoutTestAnalysis checkoutTestAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, libraryCard, pin);
        final Result result = checkoutTestAnalysis.getResult();

        switch (result) {
            case NEW_LOAN:
                final ContentProviderLoan contentProviderLoan = contentProviderDataAccessorFacade.createLoan(ehubConsumer, libraryCard, pin, pendingLoan, language);
                final LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, contentProviderLoan.getExpirationDate(), libraryCard, pin);
                final EhubLoan ehubLoan = ehubLoanRepositoryFacade.saveEhubLoan(ehubConsumer, lmsLoan, contentProviderLoan);
                return readyLoanFactory.createReadyLoan(ehubLoan, contentProviderLoan);
            case ACTIVE_LOAN:
                final String lmsLoanId = checkoutTestAnalysis.getLmsLoanId();
                return getReadyLoan(ehubConsumer, libraryCard, pin, lmsLoanId, language);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    private ReadyLoan getReadyLoan(final EhubConsumer ehubConsumer, final String libraryCard, final String pin, final String lmsLoanId, final String language) {
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        return makeReadyLoan(ehubConsumer, libraryCard, pin, ehubLoan, language);
    }

    private ReadyLoan makeReadyLoan(final EhubConsumer ehubConsumer, final String libraryCard, final String pin, final EhubLoan ehubLoan, final String language) {
        final IContent content = contentProviderDataAccessorFacade.getContent(ehubConsumer, ehubLoan, libraryCard, pin, language);
        return readyLoanFactory.createReadyLoan(ehubLoan, content);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final Long readyLoanId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, readyLoanId);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        return makeReadyLoan(ehubConsumer, libraryCard, pin, ehubLoan, language);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final String lmsLoanId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final String libraryCard = authInfo.getLibraryCard();
        final String pin = authInfo.getPin();
        return getReadyLoan(ehubConsumer, libraryCard, pin, lmsLoanId, language);
    }
}
