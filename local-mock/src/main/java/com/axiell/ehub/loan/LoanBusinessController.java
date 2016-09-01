package com.axiell.ehub.loan;

import com.axiell.ehub.AbstractBusinessController;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgumentType;
import com.axiell.ehub.Fields;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.checkout.Checkout;
import com.axiell.ehub.checkout.CheckoutDTO;
import com.axiell.ehub.checkout.CheckoutMetadataDTO;
import com.axiell.ehub.checkout.CheckoutsSearchResult;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.search.SearchResultDTO;
import com.axiell.ehub.security.AuthInfo;
import com.axiell.ehub.util.EhubMessageUtility;
import org.springframework.beans.factory.annotation.Autowired;


public class LoanBusinessController extends AbstractBusinessController implements ILoanBusinessController {
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private EhubMessageUtility ehubMessageUtility;

    @Override
    public CheckoutsSearchResult search(final AuthInfo authInfo, final String lmsLoanId, final String language) {
        SearchResultDTO<CheckoutMetadataDTO> searchResultDTO = ehubMessageUtility.getEhubMessage(SearchResultDTO.class, "search",
                String.valueOf(lmsLoanId), authInfo.getPatron().getLibraryCard());
        return new CheckoutsSearchResult(searchResultDTO==null ? new SearchResultDTO<>() : searchResultDTO);
    }

    @Override
    public Checkout checkout(final AuthInfo authInfo, final Fields fields, final String language) {
        String lmsRecordId = fields.getRequiredValue("lmsRecordId");
        String contentProviderAlias = fields.getRequiredValue("contentProviderAlias");
        String contentProviderRecordId = fields.getRequiredValue("contentProviderRecordId");
        String contentProviderIssueId = fields.getValue("contentProviderIssueId");
        String contentProviderFormatId = fields.getRequiredValue("contentProviderFormatId");
        CheckoutDTO checkoutDTO = ehubMessageUtility.getEhubMessage(CheckoutDTO.class, "checkout", contentProviderAlias, contentProviderRecordId,
                contentProviderIssueId, contentProviderFormatId, authInfo.getPatron().getLibraryCard());
        if (checkoutDTO == null) {
            ContentProviderConsumer contentProviderConsumer=getContentProviderConsumer(contentProviderAlias);
            throw ehubExceptionFactory.createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer,
                    ErrorCauseArgumentType.CREATE_LOAN_FAILED, language);
        }
        return new Checkout(checkoutDTO);
    }

    @Override
    public Checkout getCheckout(final AuthInfo authInfo, final long readyLoanId, final String language) {
        CheckoutDTO checkoutDTO = ehubMessageUtility.getEhubMessage(CheckoutDTO.class, "checkout", String.valueOf(readyLoanId),
                authInfo.getPatron().getLibraryCard());
        if (checkoutDTO == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(ErrorCauseArgument.Type.READY_LOAN_ID, readyLoanId);
            throw new NotFoundException(ErrorCause.LOAN_BY_ID_NOT_FOUND, argument);
        }
        return new Checkout(checkoutDTO);
    }
}
