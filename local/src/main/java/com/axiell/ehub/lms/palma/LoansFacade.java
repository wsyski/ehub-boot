package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.*;
import com.axiell.arena.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
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
    public CheckOutTestResponse checkOutTest(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Patron patron) {
        CheckOutTest checkOutTest = createCheckOutTest(ehubConsumer, pendingLoan, patron);
        Loans loans = loansPortFactory.getInstance(ehubConsumer);
        return loans.checkOutTest(checkOutTest);
    }

    private CheckOutTest createCheckOutTest(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Patron patron) {
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
        com.axiell.arena.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOutTest checkOutTest = loansObjectFactory.createCheckOutTest();
        checkOutTest.setCheckOutTestRequest(checkOutTestRequest);
        return checkOutTest;
    }

    @Override
    public CheckOutResponse checkOut(EhubConsumer ehubConsumer, PendingLoan pendingLoan, Date expirationDate, Patron patron) {
        CheckOut checkOut = createCheckOut(ehubConsumer, pendingLoan, expirationDate, patron);
        Loans loans = loansPortFactory.getInstance(ehubConsumer);
        return loans.checkOut(checkOut);
    }

    private static CheckOut createCheckOut(final EhubConsumer ehubConsumer, final PendingLoan pendingLoan, final Date expirationDate, final Patron patron) {
        String agencyMemberIdentifier = ehubConsumer.getProperties().get(ARENA_AGENCY_M_IDENTIFIER);
        CheckOutRequest checkOutRequest = CHECKOUTREQUEST_OBJECT_FACTORY.createCheckOutRequest();
        checkOutRequest.setArenaMember(agencyMemberIdentifier);
        checkOutRequest.setRecordId(pendingLoan.lmsRecordId());
        checkOutRequest.setExpirationDate(XjcSupport.toXmlGregorianCalendar(expirationDate));
        checkOutRequest.setContentProviderFormatId(pendingLoan.contentProviderFormatId());
        checkOutRequest.setContentProviderName(pendingLoan.contentProviderAlias());
        checkOutRequest.setUser(patron.getLibraryCard());
        checkOutRequest.setPassword(patron.getPin());
        com.axiell.arena.services.palma.loans.ObjectFactory loansObjectFactory = new com.axiell.arena.services.palma.loans.ObjectFactory();
        CheckOut checkOut = loansObjectFactory.createCheckOut();
        checkOut.setCheckOutRequest(checkOutRequest);
        return checkOut;
    }
}
