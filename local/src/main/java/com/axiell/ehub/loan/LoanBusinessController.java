package com.axiell.ehub.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link ILoanBusinessController}.
 */
public class LoanBusinessController implements ILoanBusinessController {
    @Autowired
    private IConsumerBusinessController consumerBusinessController;

    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    @Autowired
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;

    @Autowired
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;

    @Autowired
    private ICheckoutMetadataFactory checkoutMetadataFactory;

    @Autowired
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
                final Content content = contentProviderLoan.content();
                return checkoutFactory.create(ehubLoan, content, language);
            case ACTIVE_LOAN:
                final String lmsLoanId = checkoutTestAnalysis.getLmsLoanId();
                return getCheckout(ehubConsumer, patron, lmsLoanId, language);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    private Checkout getCheckout(final EhubConsumer ehubConsumer, final Patron patron, final String lmsLoanId, final String language) {
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        if (ehubLoan == null) {
            throw new InternalServerErrorException("Missing ehub loan lmsLoanId: '" + lmsLoanId + "'");
        }
        return makeCheckout(ehubConsumer, patron, ehubLoan, language);
    }

    private Checkout makeCheckout(final EhubConsumer ehubConsumer, final Patron patron, final EhubLoan ehubLoan, final String language) {
        final Content content = contentProviderDataAccessorFacade.getContent(ehubConsumer, ehubLoan, patron, language);
        return checkoutFactory.create(ehubLoan, content, language);
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
