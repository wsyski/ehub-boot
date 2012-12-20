package com.axiell.ehub.lms.palma;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProviderName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/axiell/ehub/local-context.xml")
public class PalmaDataAccessorOnlineTest {

    @Autowired
    private IPalmaDataAccessor palmaDataAccessor;

    private EhubConsumer ehubConsumer;
    private PendingLoan pendingLoan;

    @Before
    public void setUp() throws MalformedURLException {
        ehubConsumer = DevelopmentData.createEhubConsumer();
        ReflectionTestUtils.setField(ehubConsumer, "id", 1L);
        pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.name(), DevelopmentData.ELIB_RECORD_0_ID,
                DevelopmentData.ELIB_FORMAT_0_ID);
        if (!isOnline()) {
            URL arenaPalmaURL = (new File("src/main/wsdl/com/axiell/arena/palma")).toURI().toURL();
            ehubConsumer.getProperties().put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, arenaPalmaURL.toString());
        }
    }

    @Test
    public void testCheckOutTestOnline() {
        if (isOnline()) {
            PreCheckoutAnalysis preCheckoutAnalysis = palmaDataAccessor.preCheckout(ehubConsumer, pendingLoan, DevelopmentData.ELIB_LIBRARY_CARD,
                    DevelopmentData.ELIB_LIBRARY_CARD_PIN);
            assertNotNull(preCheckoutAnalysis);
        }
    }

    @Test
    public void testCheckOutOnline() {
        Date expirationDate = new Date();
        if (isOnline()) {
            LmsLoan lmsLoan = palmaDataAccessor
                    .checkout(ehubConsumer, pendingLoan, expirationDate, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN);
            assertNotNull(lmsLoan);
        }
    }

    protected static boolean isOnline() {
        return System.getProperty("online") != null && Boolean.valueOf(System.getProperty("online"));
    }
}
