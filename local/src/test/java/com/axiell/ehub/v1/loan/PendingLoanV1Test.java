package com.axiell.ehub.v1.loan;

import com.axiell.ehub.v1.XjcSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;

public class PendingLoanV1Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingLoanV1Test.class);
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private String expXml;
    private PendingLoan_v1 expPendingLoan;
    private PendingLoan_v1 actPendingLoan;

    @Before
    public void setUp() {
        expPendingLoan = new PendingLoan_v1("lmsRecordId", CONTENT_PROVIDER_TEST_EP, "9100128260", "58");
    }

    @Test
    public void unmarshalPendingLoanXml() throws JAXBException {
        givenPendingLoanAsXml();
        whenUnmarshalPeningLoanXml();
        thenActualPendingLoanEqualsExpectedPendingLoan();
    }

    private void givenPendingLoanAsXml() {
        expXml = XjcSupport.marshal(expPendingLoan);
        LOGGER.debug(expXml);
    }

    private void whenUnmarshalPeningLoanXml() throws JAXBException {
        actPendingLoan = (PendingLoan_v1) XjcSupport.unmarshal(expXml);
    }

    private void thenActualPendingLoanEqualsExpectedPendingLoan() {
        Assert.assertEquals(expPendingLoan.getLmsRecordId(), actPendingLoan.getLmsRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderName(), actPendingLoan.getContentProviderName());
        Assert.assertEquals(expPendingLoan.getContentProviderRecordId(), actPendingLoan.getContentProviderRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderFormatId(), actPendingLoan.getContentProviderFormatId());
    }
}
