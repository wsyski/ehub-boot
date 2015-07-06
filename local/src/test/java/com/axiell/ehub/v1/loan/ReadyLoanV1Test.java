package com.axiell.ehub.v1.loan;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.ContentDisposition;
import com.axiell.ehub.v1.XjcSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class ReadyLoanV1Test {
    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyLoanV1Test.class);
    private String expXml;
    private ReadyLoan_v1 expReadyLoan;
    private ReadyLoan_v1 actReadyLoan;

    @Before
    public void setUp() {
        LmsLoan_v1 lmsLoan = new LmsLoan_v1("lmsLoanId");
        ContentProviderLoan_v1 contentProviderLoan = initContentProviderLoan();
        expReadyLoan = new ReadyLoan_v1(1L, lmsLoan, contentProviderLoan);
    }

    private ContentProviderLoan_v1 initContentProviderLoan() {
        ContentProviderLoanMetadata_v1 contentProviderLoanMetadata = initContentProviderLoanMetadata();
        IContent_v1 content = new StreamingContent_v1("url", 100, 100);
        return new ContentProviderLoan_v1(contentProviderLoanMetadata, content);
    }

    private ContentProviderLoanMetadata_v1 initContentProviderLoanMetadata() {
        ContentProvider contentProvider = initContentProvider();
        FormatDecoration formatDecoration = new FormatDecoration(contentProvider, "58", ContentDisposition.DOWNLOADABLE, 10, 10);
        return new ContentProviderLoanMetadata_v1.Builder(contentProvider, new Date(), "contentProviderRecordId", formatDecoration).contentProviderLoanId(
                "contentProviderLoanId").build();
    }

    private ContentProvider initContentProvider() {
        Map<ContentProviderPropertyKey, String> properties = Collections.singletonMap(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, "productUrl");
        return new ContentProvider(CONTENT_PROVIDER_TEST_EP, properties);
    }

    @Test
    public void unmarshalReadyLoanXml() throws JAXBException {
        givenReadyLoanAsXml();
        whenUnmarshalReadyLoanXml();
        thenActualReadyLoanEqualsExpectedReadyLoan();
    }

    private void givenReadyLoanAsXml() {
        expXml = XjcSupport.marshal(expReadyLoan);
        LOGGER.debug(expXml);
    }

    private void whenUnmarshalReadyLoanXml() throws JAXBException {
        actReadyLoan = (ReadyLoan_v1) XjcSupport.unmarshal(expXml);
    }

    private void thenActualReadyLoanEqualsExpectedReadyLoan() {
        thenActualLmsLoanEqualsExpectedLmsLoan();
        thenActualContentProviderLoanEqualsExpectedContentProviderLoan();
    }

    private void thenActualLmsLoanEqualsExpectedLmsLoan() {
        Assert.assertEquals(expReadyLoan.getLmsLoan(), actReadyLoan.getLmsLoan());
    }

    private void thenActualContentProviderLoanEqualsExpectedContentProviderLoan() {
        ContentProviderLoan_v1 expContentProviderLoan = expReadyLoan.getContentProviderLoan();
        ContentProviderLoan_v1 actContentProviderLoan = actReadyLoan.getContentProviderLoan();
        Assert.assertEquals(expContentProviderLoan.getId(), actContentProviderLoan.getId());
        Assert.assertEquals(expContentProviderLoan.getExpirationDate(), actContentProviderLoan.getExpirationDate());

        thenActualLoanMetadataEqualsExpectedLoanMetadata(expContentProviderLoan, actContentProviderLoan);
    }

    private void thenActualLoanMetadataEqualsExpectedLoanMetadata(ContentProviderLoan_v1 expContentProviderLoan, ContentProviderLoan_v1 actContentProviderLoan) {
        ContentProviderLoanMetadata_v1 expMetadata = expContentProviderLoan.getMetadata();
        ContentProviderLoanMetadata_v1 actMetadata = actContentProviderLoan.getMetadata();
        Assert.assertNotNull(actMetadata.getId());
        Assert.assertNotNull(actMetadata.getExpirationDate());
        Assert.assertEquals(expMetadata.getId(), actMetadata.getId());
        Assert.assertEquals(expMetadata.getExpirationDate(), actMetadata.getExpirationDate());
        Assert.assertNull(actMetadata.getFormatDecoration());
        Assert.assertNull(actMetadata.getContentProvider());
    }
}
