package com.axiell.ehub.lms.palma;

import com.axiell.arena.services.palma.loans.*;
import com.axiell.arena.services.palma.patron.checkoutrequest.CheckOutRequest;
import com.axiell.arena.services.palma.patron.checkouttestrequest.CheckOutTestRequest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.provider.ContentProviderName;
import junit.framework.Assert;

import javax.jws.WebService;

@WebService(serviceName = "LoansPalmaService", portName = "loans", targetNamespace = "http://loans.palma.services.arena.axiell.com/",
        wsdlLocation = "com/axiell/ehub/lms/palma_266/loans.wsdl", endpointInterface = "com.axiell.arena.services.palma.loans.Loans")
public class PalmaLoansService_266 extends AbstractPalmaService implements Loans {
    private static final String CONTEXT_PATH = "com.axiell.arena.services.palma.loans";

    @Override
    public com.axiell.arena.services.palma.loans.CheckOutTestResponse checkOutTest(final CheckOutTest parameters) {
        verifyCheckOutTest(parameters);
        return getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_TEST_RESPONSE_XML);
    }

    @Override
    public com.axiell.arena.services.palma.loans.CheckOutResponse checkOut(final CheckOut parameters) {
        verifyCheckOut(parameters);
        return getFileResponseUnmarshaller().unmarshalFromFile(PALMA_CHECK_OUT_RESPONSE_XML);
    }

    @Override
    public com.axiell.arena.services.palma.loans.RenewLoansResponse renewLoans(final RenewLoans parameters) {
        return null;
    }

    @Override
    public com.axiell.arena.services.palma.loans.GetLoansResponse getLoans(final GetLoans parameters) {
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
        Assert.assertEquals(checkOutTestRequest.getContentProviderFormatId(), DevelopmentData.ELIB_FORMAT_0_ID);
        Assert.assertEquals(checkOutTestRequest.getContentProviderName(), ContentProviderName.ELIB.name());
    }

    protected void verifyCheckOut(final CheckOut checkOut) {
        CheckOutRequest checkOutRequest = checkOut.getCheckOutRequest();
        Assert.assertNotNull(checkOutRequest);
        Assert.assertEquals(checkOutRequest.getArenaMember(), DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        Assert.assertEquals(checkOutRequest.getRecordId(), DevelopmentData.LMS_RECORD_ID);
        Assert.assertEquals(checkOutRequest.getUser(), DevelopmentData.ELIB_LIBRARY_CARD);
        Assert.assertEquals(checkOutRequest.getPassword(), DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        Assert.assertEquals(checkOutRequest.getContentProviderFormatId(), DevelopmentData.ELIB_FORMAT_0_ID);
        Assert.assertEquals(checkOutRequest.getContentProviderName(), ContentProviderName.ELIB.name());
    }
}