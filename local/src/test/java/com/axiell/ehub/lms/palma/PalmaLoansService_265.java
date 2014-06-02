package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma_265.loans.*;
import com.axiell.arena.services.palma_265.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma_265.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.ehub.DevelopmentData;
import junit.framework.Assert;

import javax.jws.WebService;

@WebService(serviceName = "LoansPalmaService", portName = "loans", targetNamespace = "http://loans.palma.services.ehub.axiell.com/",
        wsdlLocation = "com/axiell/ehub/lms/palma_265/loans.wsdl", endpointInterface = "com.axiell.arena.services.palma_265.loans.Loans")
public class PalmaLoansService_265 extends AbstractPalmaLoansService implements Loans {
    private static final String CONTEXT_PATH = "com.axiell.ehub.services.palma_265.loans";

    @Override
    public CheckOutTestResponse checkOutTest(final CheckOutTest parameters) {
        verifyCheckOutTest(parameters);
        return getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_TEST_RESPONSE_XML);
    }

    @Override
    public CheckOutResponse checkOut(final CheckOut parameters) {
        verifyCheckOut(parameters);
        return getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_RESPONSE_XML);
    }

    @Override
    public RenewLoansResponse renewLoans(final RenewLoans parameters) {
        return null;
    }

    @Override
    public GetLoansResponse getLoans(final GetLoans parameters) {
        return null;
    }

    @Override
    protected String getContextPath() {
        return CONTEXT_PATH;
    }

    protected void verifyCheckOutTest(final CheckOutTest checkOutTest) {
        CheckOutTestRequest checkOutTestRequest = checkOutTest.getCheckOutTestRequest();
        Assert.assertNotNull(checkOutTestRequest);
        Assert.assertEquals(checkOutTestRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutTestRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutTestRequest.getUser(), DevelopmentData.ELIB_LIBRARY_CARD);
        Assert.assertEquals(checkOutTestRequest.getPassword(), DevelopmentData.ELIB_LIBRARY_CARD_PIN);
    }

    protected void verifyCheckOut(final CheckOut checkOut) {
        CheckOutRequest checkOutRequest = checkOut.getCheckOutRequest();
        Assert.assertNotNull(checkOutRequest);
        Assert.assertEquals(checkOutRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutRequest.getUser(), DevelopmentData.ELIB_LIBRARY_CARD);
        Assert.assertEquals(checkOutRequest.getPassword(), DevelopmentData.ELIB_LIBRARY_CARD_PIN);
    }
}