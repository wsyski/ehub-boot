package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;
import net.javacrumbs.smock.common.client.CommonSmockClient;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.http.client.connection.SmockClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.test.client.RequestMatchers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/com/axiell/ehub/common-context.xml")
public class PalmaDataAccessorTest {
    /*
    private static final ApplicationContext SPRING_CONTEXT = new ClassPathXmlApplicationContext(new String[]{
            "classpath:/com/axiell/ehub/common-context.xml"});
    */
    //@Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;

    private MockWebServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        //palmaDataAccessor=SPRING_CONTEXT.getBean(IPalmaDataAccessor.class);
        palmaDataAccessor = new PalmaDataAccessor();
        ehubConsumer = createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        URL arenaPalmaURL = (new File("src/main/wsdl/com/axiell/arena/palma")).toURI().toURL();
        ehubConsumer.getProperties().put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, arenaPalmaURL.toString());
        mockServer = SmockClient.createServer(new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
    }

    @After
    public void tearDown() {
        mockServer.verify();
    }

    @Test
    public void test() {

    }
    //@Test
    public void testCheckOutTest() {
        mockServer.expect(RequestMatchers.anything()).andRespond(CommonSmockClient.withMessage("/com/axiell/arena/palma/CheckOutTestResponse.xml"));
        PreCheckoutAnalysis preCheckoutAnalysis =
                palmaDataAccessor.preCheckout(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(preCheckoutAnalysis);
        assertEquals(PreCheckoutAnalysis.Result.ACTIVE_LOAN, preCheckoutAnalysis.getResult());
        assertEquals(DevelopmentData.LMS_LOAN_ID, preCheckoutAnalysis.getLmsLoanId());
    }

    //@Test
    public void testCheckOut() {
        Date expirationDate = new Date();
        mockServer.expect(RequestMatchers.anything()).andRespond(CommonSmockClient.withMessage("/com/axiell/arena/palma/CheckOutResponse.xml"));
        LmsLoan lmsLoan = palmaDataAccessor.checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD,
                DevelopmentData.ELIB_LIBRARY_CARD_PIN);
        assertNotNull(lmsLoan);
        assertEquals(DevelopmentData.LMS_LOAN_ID, lmsLoan.getId());
    }


    public EhubConsumer createEhubConsumer() {
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, DevelopmentData.ARENA_PALMA_URL);
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_AGENCY_M_IDENTIFIER, DevelopmentData.ARENA_AGENCY_M_IDENTIFIER);
        EhubConsumer ehubConsumer = new EhubConsumer("Ehub Consumer Description", DevelopmentData.EHUB_CONSUMER_SECRET_KEY, ehubConsumerProperties);
        return ehubConsumer;
    }

}
