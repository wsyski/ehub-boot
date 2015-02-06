/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.checkout.CheckoutMetadata;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.checkout.ICheckoutMetadataFactory;
import com.axiell.ehub.patron.Patron;
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

    @Autowired(required = true)
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    @Override
    @Transactional(readOnly = true)
    public CheckoutsSearchResult search(AuthInfo authInfo, String lmsLoanId, String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        final CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, language);
        return new CheckoutsSearchResult().addItem(checkoutMetadata);
    }

    @Override
    @Transactional(readOnly = false)
    public ReadyLoan createLoan(final AuthInfo authInfo, final PendingLoan pendingLoan, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final Patron patron = authInfo.getPatron();
        final CheckoutTestAnalysis checkoutTestAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, patron);
        final Result result = checkoutTestAnalysis.getResult();

        switch (result) {
            case NEW_LOAN:
                final ContentProviderLoan contentProviderLoan = contentProviderDataAccessorFacade.createLoan(ehubConsumer, patron, pendingLoan, language);
                final LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, contentProviderLoan.getExpirationDate(), patron);
                final EhubLoan ehubLoan = ehubLoanRepositoryFacade.saveEhubLoan(ehubConsumer, lmsLoan, contentProviderLoan);
                return readyLoanFactory.createReadyLoan(ehubLoan, contentProviderLoan);
            case ACTIVE_LOAN:
                final String lmsLoanId = checkoutTestAnalysis.getLmsLoanId();
                return getReadyLoan(ehubConsumer, patron, lmsLoanId, language);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    private ReadyLoan getReadyLoan(final EhubConsumer ehubConsumer, final Patron patron, final String lmsLoanId, final String language) {
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        return makeReadyLoan(ehubConsumer, patron, ehubLoan, language);
    }

    private ReadyLoan makeReadyLoan(final EhubConsumer ehubConsumer, final Patron patron, final EhubLoan ehubLoan, final String language) {
        final IContent content = contentProviderDataAccessorFacade.getContent(ehubConsumer, ehubLoan, patron, language);
        return readyLoanFactory.createReadyLoan(ehubLoan, content);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final Long readyLoanId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, readyLoanId);
        final Patron patron = authInfo.getPatron();
        return makeReadyLoan(ehubConsumer, patron, ehubLoan, language);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public ReadyLoan getReadyLoan(final AuthInfo authInfo, final String lmsLoanId, final String language) {
//        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
//        final Patron patron = authInfo.getPatron();
//        return getReadyLoan(ehubConsumer, patron, lmsLoanId, language);
//    }
}
