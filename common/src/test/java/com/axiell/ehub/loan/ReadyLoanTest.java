package com.axiell.ehub.loan;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.util.XjcSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class ReadyLoanTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyLoanTest.class);
    private String expXml;
    private ReadyLoan expReadyLoan;
    private ReadyLoan actReadyLoan;

    @Before
    public void setUp() {
        LmsLoan lmsLoan = new LmsLoan("lmsLoanId");
        ContentProviderLoan contentProviderLoan = initContentProviderLoan();
        expReadyLoan = new ReadyLoan(1L, lmsLoan, contentProviderLoan);
    }

    private ContentProviderLoan initContentProviderLoan() {
        ContentProviderLoanMetadata contentProviderLoanMetadata = initContentProviderLoanMetadata();
        IContent content = new StreamingContent("url", 100, 100);
        return new ContentProviderLoan(contentProviderLoanMetadata, content);
    }

    private ContentProviderLoanMetadata initContentProviderLoanMetadata() {
        ContentProvider contentProvider = initContentProvider();
        FormatDecoration formatDecoration = new FormatDecoration(contentProvider, "58", ContentDisposition.DOWNLOADABLE, 10, 10);
        return new ContentProviderLoanMetadata.Builder(contentProvider, new Date(), "contentProviderRecordId", formatDecoration).contentProviderLoanId(
                "contentProviderLoanId").build();
    }

    private ContentProvider initContentProvider() {
        Map<ContentProviderPropertyKey, String> properties = Collections.singletonMap(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, "productUrl");
        return new ContentProvider(ContentProviderName.ELIB, properties);
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
        actReadyLoan = (ReadyLoan) XjcSupport.unmarshal(expXml);
    }

    private void thenActualReadyLoanEqualsExpectedReadyLoan() {
        thenActualLmsLoanEqualsExpectedLmsLoan();
        thenActualContentProviderLoanEqualsExpectedContentProviderLoan();
    }

    private void thenActualLmsLoanEqualsExpectedLmsLoan() {
        Assert.assertEquals(expReadyLoan.getLmsLoan(), actReadyLoan.getLmsLoan());
    }

    private void thenActualContentProviderLoanEqualsExpectedContentProviderLoan() {
        ContentProviderLoan expContentProviderLoan = expReadyLoan.getContentProviderLoan();
        ContentProviderLoan actContentProviderLoan = actReadyLoan.getContentProviderLoan();
        Assert.assertEquals(expContentProviderLoan.getId(), actContentProviderLoan.getId());
        Assert.assertEquals(expContentProviderLoan.getExpirationDate(), actContentProviderLoan.getExpirationDate());

        thenActualLoanMetadataEqualsExpectedLoanMetadata(expContentProviderLoan, actContentProviderLoan);
    }

    private void thenActualLoanMetadataEqualsExpectedLoanMetadata(ContentProviderLoan expContentProviderLoan, ContentProviderLoan actContentProviderLoan) {
        ContentProviderLoanMetadata expMetadata = expContentProviderLoan.getMetadata();
        ContentProviderLoanMetadata actMetadata = actContentProviderLoan.getMetadata();
        Assert.assertNotNull(actMetadata.getId());
        Assert.assertNotNull(actMetadata.getExpirationDate());
        Assert.assertEquals(expMetadata.getId(), actMetadata.getId());
        Assert.assertEquals(expMetadata.getExpirationDate(), actMetadata.getExpirationDate());
        Assert.assertNull(actMetadata.getFormatDecoration());
        Assert.assertNull(actMetadata.getContentProvider());
    }
}
