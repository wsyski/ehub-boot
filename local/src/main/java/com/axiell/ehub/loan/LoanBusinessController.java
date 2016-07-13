package com.axiell.ehub.loan;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.IFormatDecorationRepositoryFacade;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    private IFormatDecorationRepositoryFacade formatDecorationRepositoryFacade;

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
            final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
            final CheckoutMetadata checkoutMetadata = checkoutMetadataFactory.create(ehubLoan, contentProviderLoanMetadata.getFirstFormatDecoration(), language,
                    false);
            checkoutsSearchResult.addItem(checkoutMetadata);
        }
        return checkoutsSearchResult;
    }

    @Override
    @Transactional
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        final PendingLoan pendingLoan = new PendingLoan(fields);
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final Patron patron = authInfo.getPatron();
        final String contentProviderAlias = pendingLoan.contentProviderAlias();
        final ContentProviderConsumer contentProviderConsumer =
                contentProviderDataAccessorFacade.getContentProviderConsumer(ehubConsumer, contentProviderAlias);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final boolean isLoanPerProduct = contentProvider.isLoanPerProduct();
        final CheckoutTestAnalysis checkoutTestAnalysis = palmaDataAccessor.checkoutTest(ehubConsumer, pendingLoan, patron, isLoanPerProduct);
        final Result result = checkoutTestAnalysis.getResult();

        switch (result) {
            case NEW_LOAN:
                final ContentProviderLoan contentProviderLoan = contentProviderDataAccessorFacade.createLoan(ehubConsumer, patron, pendingLoan, language);
                final LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, contentProviderLoan.expirationDate(), patron, isLoanPerProduct);
                final EhubLoan ehubLoan = ehubLoanRepositoryFacade.saveEhubLoan(ehubConsumer, lmsLoan, contentProviderLoan);
                final Content content = contentProviderLoan.content();
                final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
                return checkoutFactory.create(ehubLoan, contentProviderLoanMetadata.getFirstFormatDecoration(), content, language, true);
            case ACTIVE_LOAN:
                final String lmsLoanId = checkoutTestAnalysis.getLmsLoanId();
                return findCheckout(ehubConsumer, patron, lmsLoanId, pendingLoan.contentProviderFormatId(), language);
            default:
                throw new NotImplementedException("Create loan where the result of the pre-checkout analysis is '" + result + "' has not been implemented");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Checkout getCheckout(final AuthInfo authInfo, final long readyLoanId, final String language) {
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(authInfo);
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(readyLoanId);
        final Patron patron = authInfo.getPatron();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
        final FormatDecoration firstFormatDecoration = contentProviderLoanMetadata.getFirstFormatDecoration();
        return makeCheckout(ehubConsumer, patron, ehubLoan, firstFormatDecoration, language);
    }

    private Checkout findCheckout(final EhubConsumer ehubConsumer, final Patron patron, final String lmsLoanId, final String contentProviderFormatId,
                                  final String language) {
        final EhubLoan ehubLoan = ehubLoanRepositoryFacade.findEhubLoan(ehubConsumer, lmsLoanId);
        FormatDecoration formatDecoration = getFormatDecoration(contentProviderFormatId, ehubLoan);
        return makeCheckout(ehubConsumer, patron, ehubLoan, formatDecoration, language);
    }

    private FormatDecoration getFormatDecoration(final String contentProviderFormatId, final EhubLoan ehubLoan) {
        final ContentProviderLoanMetadata contentProviderLoanMetadata = ehubLoan.getContentProviderLoanMetadata();
        final FormatDecoration firstFormatDecoration = contentProviderLoanMetadata.getFirstFormatDecoration();
        final FormatDecoration formatDecoration;
        if (!firstFormatDecoration.getContentProviderFormatId().equals(contentProviderFormatId)) {
            ContentProvider contentProvider = contentProviderLoanMetadata.getContentProvider();
            if (contentProvider.isLoanPerProduct()) {
                formatDecoration = formatDecorationRepositoryFacade.find(contentProvider, contentProviderFormatId);
            } else {
                final ErrorCauseArgument argument = new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProvider.getName());
                throw new NotFoundException(ErrorCause.CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT, argument);
            }
        } else {
            formatDecoration = firstFormatDecoration;
        }
        return formatDecoration;
    }

    private Checkout makeCheckout(final EhubConsumer ehubConsumer, final Patron patron, final EhubLoan ehubLoan, final FormatDecoration formatDecoration,
                                  final String language) {
        final Content content = contentProviderDataAccessorFacade.getContent(ehubConsumer, ehubLoan, formatDecoration, patron, language);
        return checkoutFactory.create(ehubLoan, formatDecoration, content, language, false);
    }
}
