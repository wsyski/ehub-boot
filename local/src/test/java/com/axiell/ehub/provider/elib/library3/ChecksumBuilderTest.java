package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import junit.framework.Assert;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ChecksumBuilderTest {
//    private static final String SERVICE_ID = "1";
//    private static final String SERVICE_KEY = "secret";
//    private static final String PRODUCT_ID = "10024451002443";
//    private static final String PATRON_ID = "12345";

    private static final String SERVICE_ID = "1873";
    private static final String SERVICE_KEY = "Vm3qh9eZijFdMDxAEpn5PzyfC0S4sBGOvXtJrIYw1Ukl8cuoR2";
    private static final String PRODUCT_ID = "1000001";
    private static final String PATRON_ID = "1";
    private static final String EXP_CHECSUM_GET_PRODUCT = "1C8A62AB7B32CB690A7EB95CF75DFBA79BB62AB3A2DDCBD402B96D5ABD9EA99167E78A7A2E7BACC889821783BCBBBE12FDC856576FB2A1D57AE118058DECA02A";
    private static final String EXP_CHECSUM_GET_PRODUCTS = "54cee6c58a7de58d661a03e91bc432cad6dda2e2c5f565b1e51249495a0aea16d13ecc26cfc9c31dda70f673ef28bfaaa829d48af617bba9cd2407d0ffc2af26";

    private ChecksumBuilder underTest;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    private String actualChecksum;

    @Before
    public void setUp() {
        givenSecretKey();
        underTest = new ChecksumBuilder(SERVICE_ID, contentProviderConsumer);
    }

    @Test
    public void checksumForGetProduct() {
        givenProductIdAppendedToChecksumBuilder();
        whenBuildChecksum();
        thenActualChecksumEqualsExpectedChecksumForGetProduct();
    }

    @Test
    public void chechsumForGetProducts() {
        underTest.appendParameter("2014-05-16");
        whenBuildChecksum();
        Assert.assertEquals(EXP_CHECSUM_GET_PRODUCTS, actualChecksum);
    }

    @Test
    public void checksumForBookAvailability() {
        underTest.appendParameter(PATRON_ID).appendParameter(PRODUCT_ID);
        whenBuildChecksum();
        System.out.println(actualChecksum);
    }

    private void givenProductIdAppendedToChecksumBuilder() {
        underTest.appendParameter(PRODUCT_ID);
    }

    private void whenBuildChecksum() {
        actualChecksum = underTest.build();
    }

    private void thenActualChecksumEqualsExpectedChecksumForGetProduct() {
        System.out.println(actualChecksum);
        assertThat(actualChecksum, IsEqualIgnoringCase.equalToIgnoringCase(EXP_CHECSUM_GET_PRODUCT));
    }

    private void givenSecretKey() {
        given(contentProviderConsumer.getProperty(any(ContentProviderConsumer.ContentProviderConsumerPropertyKey.class))).willReturn(SERVICE_KEY);
    }
}
