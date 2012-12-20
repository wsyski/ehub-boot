package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.http.client.connection.SmockClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.anything;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/axiell/ehub/local-context.xml")
public class PalmaDataAccessorTest {

    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    private MockWebServiceServer mockWebServiceServer;
    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;

    @Before
    public void setUp() throws MalformedURLException {
        ehubConsumer = DevelopmentData.createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        URL arenaPalmaURL = (new File("src/main/wsdl/com/axiell/arena/palma")).toURI().toURL();
        ehubConsumer.getProperties().put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, arenaPalmaURL.toString());
        mockWebServiceServer = createMockWebServiceServer();
    }

    public void tearDown() {
        mockWebServiceServer.verify();
    }

    @Test
    public void testCheckOutTest() {
        mockWebServiceServer.expect(anything()).andRespond(SmockClient.withMessage("/com/axiell/arena/palma/CheckOutTestResponse.xml"));
        PreCheckoutAnalysis preCheckoutAnalysis =
                palmaDataAccessor.preCheckout(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(preCheckoutAnalysis);
        assertEquals(PreCheckoutAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID, preCheckoutAnalysis.getLmsLoanId());
    }

    @Test
    public void testCheckOut() {
        Date expirationDate = new Date();
        MockWebServiceServer mockWebServiceServer = createMockWebServiceServer();
        mockWebServiceServer.expect(anything()).andRespond(SmockClient.withMessage("/com/axiell/arena/palma/CheckOutResponse.xml"));
        LmsLoan lmsLoan =
                palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(lmsLoan);
        assertEquals(DevelopmentData.LMS_LOAN_ID, lmsLoan.getId());
    }

    private MockWebServiceServer createMockWebServiceServer() {
        return SmockClient.createServer(new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
    }
}
