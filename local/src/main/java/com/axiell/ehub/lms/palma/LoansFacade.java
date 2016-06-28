package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.Loans;
import com.axiell.arena.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma.patron.checkoutresponse.CheckOutResponse;
import com.axiell.arena.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.arena.services.palma.patron.checkouttestresponse.CheckOutTestResponse;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.util.XjcSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER;

@Component
class LoansFacade implements ILoansFacade {
    @Autowired
    private ILoansPortFactory loansPortFactory;

    private static final com.axiell.arena.services.palma.patron.checkoutrequest.ObjectFactory CHECKOUTREQUEST_OBJECT_FACTORY =
            new com.axiell.arena.services.palma.patron.checkoutrequest.ObjectFactory();

    @Override
    public CheckOutTestResponse checkOutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, Patron patron, final boolean isLoanPerProduct) {
        CheckOutTestRequest checkOutTest = createCheckOutTestRequest(ehubConsumer, pendingLoan, patron, isLoanPerProduct);
        Loans loans = loansPortFactory.getInstance(ehubConsumer);
        return loans.checkOutTest(checkOutTest);
    }

    private CheckOutTestRequest createCheckOutTestRequest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron,
                                                          final boolean isLoanPerProduct) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        com.axiell.arena.services.palma.patron.checkouttestrequest.ObjectFactory checkoutTestRequestObjectFactory =
                new com.axiell.arena.services.palma.patron.checkouttestrequest.ObjectFactory();
        CheckOutTestRequest checkOutTestRequest = checkoutTestRequestObjectFactory.createCheckOutTestRequest();
        checkOutTestRequest.setArenaMember(agencyMemberIdentifier);
        checkOutTestRequest.setRecordId(pendingLoan.lmsRecordId());
        checkOutTestRequest.setContentProviderFormatId(pendingLoan.contentProviderFormatId());
        checkOutTestRequest.setContentProviderName(pendingLoan.contentProviderAlias());
        checkOutTestRequest.setUser(patron.getLibraryCard());
        checkOutTestRequest.setPassword(patron.getPin());
        checkOutTestRequest.setIssueId(pendingLoan.contentProviderIssueId());
        checkOutTestRequest.setLoanPerProduct(isLoanPerProduct);
        return checkOutTestRequest;
    }

    @Override
    public CheckOutResponse checkOut(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final Patron patron,
                                     final boolean isLoanPerProduct) {
        CheckOutRequest checkOutRequest = createCheckOutRequest(ehubConsumer, pendingLoan, expirationDate, patron, isLoanPerProduct);
        Loans loans = loansPortFactory.getInstance(ehubConsumer);
        return loans.checkOut(checkOutRequest);
    }

    private static CheckOutRequest createCheckOutRequest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate,
                                                         final Patron patron, final boolean isLoanPerProduct) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        CheckOutRequest checkOutRequest = CHECKOUTREQUEST_OBJECT_FACTORY.createCheckOutRequest();
        checkOutRequest.setArenaMember(agencyMemberIdentifier);
        checkOutRequest.setRecordId(pendingLoan.lmsRecordId());
        checkOutRequest.setExpirationDate(XjcSupport.toXmlGregorianCalendar(expirationDate));
        checkOutRequest.setContentProviderFormatId(pendingLoan.contentProviderFormatId());
        checkOutRequest.setContentProviderName(pendingLoan.contentProviderAlias());
        checkOutRequest.setUser(patron.getLibraryCard());
        checkOutRequest.setPassword(patron.getPin());
        checkOutRequest.setIssueId(pendingLoan.contentProviderIssueId());
        checkOutRequest.setLoanPerProduct(isLoanPerProduct);
        return checkOutRequest;
    }
}
