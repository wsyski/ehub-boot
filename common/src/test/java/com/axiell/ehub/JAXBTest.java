package com.axiell.ehub;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.LmsLoan;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.ReadyLoan;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.util.XjcSupport;

/**
 */
public class JAXBTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JAXBTest.class);
    private PendingLoan expPendingLoan;
    private ReadyLoan expReadyLoan;
    private Format expFormat1;
    private Formats expFormats;
    private EhubError expEhubError;

    @Before
    public void setUp() {
        expPendingLoan = new PendingLoan("lmsRecordId", ContentProviderName.ELIB.toString(), "9100128260", "58");
        ContentProvider contentProvider = new ContentProvider(ContentProviderName.ELIB, Collections.singletonMap(
                ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, "productUrl"));
        Map<EhubConsumer.EhubConsumerPropertyKey, String> ehubConsumerProperties = new HashMap<>();
        ehubConsumerProperties.put(EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL, "palmaUrl");
        FormatDecoration
                expContentProviderFormatDecoration = new FormatDecoration(contentProvider,"58", ContentDisposition.DOWNLOADABLE,10,10);

        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata("contentProviderLoanId", contentProvider, new Date(),
                expContentProviderFormatDecoration);
        LmsLoan lmsLoan = new LmsLoan("lmsLoanId");
        IContent content = new StreamingContent("url", 100, 100);
        ContentProviderLoan contentProviderLoan = new ContentProviderLoan(contentProviderLoanMetadata, content);
        expReadyLoan = new ReadyLoan(1L, lmsLoan, contentProviderLoan);

        expFormat1 = new Format("id1", "name1", "description1", "iconUrl1");
        expFormats = new Formats();
        expFormats.addFormat(expFormat1);
                
        final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.LMS_LOAN_ID, "unknownLmsLoanId1");
        expEhubError = ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND.toEhubError(argument1);
    }

    @Test
    public void testMarshalReadyLoan() throws Exception {
        String xml = XjcSupport.marshal(expReadyLoan);
        LOGGER.debug(xml);
        ReadyLoan actReadyLoan = (ReadyLoan) XjcSupport.unmarshal(xml);

        Assert.assertEquals(expReadyLoan.getLmsLoan(), actReadyLoan.getLmsLoan());

        ContentProviderLoan expContentProviderLoan = expReadyLoan.getContentProviderLoan();
        ContentProviderLoan actContentProviderLoan = actReadyLoan.getContentProviderLoan();
        Assert.assertEquals(expContentProviderLoan.getId(), actContentProviderLoan.getId());
        Assert.assertEquals(expContentProviderLoan.getExpirationDate(), actContentProviderLoan.getExpirationDate());

        ContentProviderLoanMetadata expMetadata = expContentProviderLoan.getMetadata();
        ContentProviderLoanMetadata actMetadata = actContentProviderLoan.getMetadata();
        Assert.assertNotNull(actMetadata.getId());
        Assert.assertNotNull(actMetadata.getExpirationDate());
        Assert.assertEquals(expMetadata.getId(), actMetadata.getId());
        Assert.assertEquals(expMetadata.getExpirationDate(), actMetadata.getExpirationDate());
        Assert.assertNull(actMetadata.getFormatDecoration());
        Assert.assertNull(actMetadata.getContentProvider());
    }

    @Test
    public void testMarshalPendingLoan() throws Exception {
        String xml = XjcSupport.marshal(expPendingLoan);
        LOGGER.debug(xml);
        PendingLoan actPendingLoan = (PendingLoan) XjcSupport.unmarshal(xml);
        Assert.assertEquals(expPendingLoan.getLmsRecordId(), actPendingLoan.getLmsRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderName(), actPendingLoan.getContentProviderName());
        Assert.assertEquals(expPendingLoan.getContentProviderRecordId(), actPendingLoan.getContentProviderRecordId());
        Assert.assertEquals(expPendingLoan.getContentProviderFormatId(), actPendingLoan.getContentProviderFormatId());
    }

    @Test
    public void testMarshalContentProviderFormats() throws Exception {
        String xml = XjcSupport.marshal(expFormats);
        LOGGER.debug(xml);
        Formats actFormats = (Formats) XjcSupport.unmarshal(xml);
        Assert.assertEquals(1, actFormats.getFormats().size());
        Format actFormat = actFormats.getFormats().iterator().next();
        Assert.assertEquals(expFormat1.getDescription(), actFormat.getDescription());
        Assert.assertEquals(expFormat1.getIconUrl(), actFormat.getIconUrl());
        Assert.assertEquals(expFormat1.getId(), actFormat.getId());
        Assert.assertEquals(expFormat1.getName(), actFormat.getName());
    }
    
    @Test
    public void testMarshalEhubError() throws Exception {
        Assert.assertNotNull(expEhubError);
        String xml = XjcSupport.marshal(expEhubError);
        LOGGER.debug(xml);
        EhubError actEhubError = XjcSupport.unmarshal(xml, EhubError.class);
        Assert.assertEquals(expEhubError.getCause(), actEhubError.getCause());
        Assert.assertEquals(expEhubError.getMessage(), actEhubError.getMessage());
        Assert.assertNotNull(actEhubError.getArguments());        
        Assert.assertEquals(expEhubError.getArguments().size(), actEhubError.getArguments().size());
    }
}
