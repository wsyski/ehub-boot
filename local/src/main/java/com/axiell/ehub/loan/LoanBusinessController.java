/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.checkout.*;
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
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    @Autowired(required = true)
    private ICheckoutFactory checkoutFactory;

    @Override
    @Transactional(readOnly = true)
    public CheckoutsSearchResult search(AuthInfo authInfo, String lmsLoanId, String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final CheckoutsSearchResult checkoutsSearchResult = new CheckoutsSearchResult();
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        if (ehubLoan != null) {
            final CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, language);
            checkoutsSearchResult.addItem(checkoutMetadata);
        }
        return checkoutsSearchResult;
    }

    @Override
    @Transactional(readOnly = false)
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final PendingLoan pendingLoan = new PendingLoan(fields);
        final Patron patron = authInfo.getPatron();
        final CheckoutTestAnalysis checkoutTestAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, patron);
        final Result result = checkoutTestAnalysis.getResult();

        switch (result) {
            case NEW_LOAN:
                final ContentProviderLoan contentProviderLoan = contentProviderDataAccessorFacade.createLoan(ehubConsumer, patron, pendingLoan, language);
                final LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, contentProviderLoan.expirationDate(), patron);
                final EhubLoan ehubLoan = ehubLoanRepositoryFacade.saveEhubLoan(ehubConsumer, lmsLoan, contentProviderLoan);
                final ContentLink contentLink = contentProviderLoan.contentLink();
                return checkoutFactory.create(ehubLoan, contentLink, language);
            case ACTIVE_LOAN:
                final String lmsLoanId = checkoutTestAnalysis.getLmsLoanId();
                return getCheckout(ehubConsumer, patron, lmsLoanId, language);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    private Checkout getCheckout(final EhubConsumer ehubConsumer, final Patron patron, final String lmsLoanId, final String language) {
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        return makeCheckout(ehubConsumer, patron, ehubLoan, language);
    }

    private Checkout makeCheckout(final EhubConsumer ehubConsumer, final Patron patron, final EhubLoan ehubLoan, final String language) {
        final ContentLink contentLink = contentProviderDataAccessorFacade.getContent(ehubConsumer, ehubLoan, patron, language);
        return checkoutFactory.create(ehubLoan, contentLink, language);
    }

    @Override
    @Transactional(readOnly = true)
    public Checkout getCheckout(final AuthInfo authInfo, final Long readyLoanId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, readyLoanId);
        final Patron patron = authInfo.getPatron();
        return makeCheckout(ehubConsumer, patron, ehubLoan, language);
    }
}
