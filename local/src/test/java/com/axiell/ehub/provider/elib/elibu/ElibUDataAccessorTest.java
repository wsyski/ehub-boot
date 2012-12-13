/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * 
 */
public class ElibUDataAccessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibUDataAccessorTest.class);
    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private ContentProviderConsumer contentProviderConsumer = mockery.mock(ContentProviderConsumer.class);
    private ContentProvider contentProvider;

    @Before
    public void setUp() throws Exception {
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CONSUME_LICENSE_URL, DevelopmentData.ELIBU_CONSUME_LICENSE_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, DevelopmentData.ELIBU_PRODUCT_URL);
        contentProvider = new ContentProvider(ContentProviderName.ELIBU, contentProviderProperties);

        FormatDecoration formatDecoration0 = new FormatDecoration(contentProvider, DevelopmentData.ELIBU_FORMAT_0_ID,
                ContentDisposition.STREAMING,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT);
        FormatDecoration formatDecoration1 = new FormatDecoration(contentProvider, DevelopmentData.ELIBU_FORMAT_1_ID,
                ContentDisposition.STREAMING,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT);

        final String language = Locale.ENGLISH.getLanguage();
        
        FormatTextBundle textBundle0 = new FormatTextBundle(formatDecoration0, language, DevelopmentData.ELIBU_FORMAT_0_NAME,
                DevelopmentData.ELIBU_FORMAT_0_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles0 = new HashMap<>();
        textBundles0.put(language, textBundle0);
        formatDecoration0.setTextBundles(textBundles0);
        
        FormatTextBundle textBundle1 = new FormatTextBundle(formatDecoration1, language, DevelopmentData.ELIBU_FORMAT_1_NAME,
                DevelopmentData.ELIBU_FORMAT_1_DESCRIPTION);
        Map<String, FormatTextBundle> textBundles1 = new HashMap<>();
        textBundles1.put(language, textBundle1);
        formatDecoration1.setTextBundles(textBundles1);        
        
        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_0_ID, formatDecoration0);
        formatDecorations.put(DevelopmentData.ELIBU_FORMAT_1_ID, formatDecoration1);

        contentProvider.setFormatDecorations(formatDecorations);

        mockery.checking(new Expectations() {
            {
                allowing(contentProviderConsumer).getProperty(with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_ID)));
                will(returnValue(DevelopmentData.ELIBU_SERVICE_ID));
                allowing(contentProviderConsumer).getProperty(with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_KEY)));
                will(returnValue(DevelopmentData.ELIBU_SERVICE_KEY));
                allowing(contentProviderConsumer).getProperty(with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.SUBSCRIPTION_ID)));
                will(returnValue(DevelopmentData.ELIBU_SUBSCRIPTION_ID));
                allowing(contentProviderConsumer).getContentProvider();
                will(returnValue(contentProvider));
            }
        });
    }

    /**
     * Test method for {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#consumeLicense(ContentProviderConsumer, String)}.
     */
    @Test
    public void testGetLicenseId() {
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        Integer actLicenseId = elibUDataAccessor.consumeLicense(contentProviderConsumer, DevelopmentData.ELIBU_LIBRARY_CARD);
        assertEquals(6, actLicenseId.intValue());
    }

    /**
     * Test method for
     * {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#createLoan(ContentProviderConsumer, String, String, PendingLoan)}.
     */
    @Test
    public void testCreateLoan() {
        final PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIBU.toString(), DevelopmentData.ELIBU_RECORD_ID,
                DevelopmentData.ELIBU_FORMAT_0_ID);
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        ContentProviderLoan actContentProviderLoan = elibUDataAccessor.createLoan(contentProviderConsumer, DevelopmentData.ELIBU_LIBRARY_CARD,
                DevelopmentData.ELIBU_LIBRARY_CARD_PIN, pendingLoan);
        assertNotNull(actContentProviderLoan);
        IContent actContent = actContentProviderLoan.getContent();
        assertNotNull(actContent);
        assertTrue(actContent instanceof StreamingContent);
        StreamingContent actStreamingContent = (StreamingContent) actContent;
        String url = actStreamingContent.getUrl();
        LOGGER.debug("ElibU Streaming URL = '" + url + "'");
        assertNotNull(url);
    }

    /**
     * Test method for
     * {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#getContent(ContentProviderConsumer, String, String, ContentProviderLoanMetadata)}
     */
    @Test
    public void testGetContent() {
        FormatDecoration formatDecoration = new FormatDecoration(contentProvider, DevelopmentData.ELIBU_FORMAT_0_ID,
                ContentDisposition.STREAMING,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT,DevelopmentData.ELIBU_EBOOK_PLAYER_WIDTH_AND_HEIGHT);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata(DevelopmentData.ELIBU_LOAN_ID, contentProvider, new Date(),
                formatDecoration);
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        IContent content = elibUDataAccessor.getContent(contentProviderConsumer, DevelopmentData.ELIBU_LIBRARY_CARD, DevelopmentData.ELIBU_LIBRARY_CARD_PIN,
                contentProviderLoanMetadata);
        assertNotNull(content);
    }

    /**
     * Test method for {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#createElibULoanId(Integer, String)}
     */
    @Test
    public void testCreateElibULoanId() {
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        String actElibULoanId = elibUDataAccessor.createElibULoanId(DevelopmentData.ELIBU_LICENSE_ID, DevelopmentData.ELIBU_RECORD_ID);
        assertEquals(DevelopmentData.ELIBU_LOAN_ID, actElibULoanId);
    }

    /**
     * Test method for {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#getElibURecordId(String)}
     */
    @Test
    public void testGetElibURecordId() {
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        String actElibURecordId = elibUDataAccessor.getElibURecordId(DevelopmentData.ELIBU_LOAN_ID);
        assertEquals(DevelopmentData.ELIBU_RECORD_ID, actElibURecordId);
    }

    /**
     * Test method for {@link com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor#getFormats(ContentProviderConsumer, String, String)}
     */
    @Test
    public void testGetFormats() {
        ElibUDataAccessor elibUDataAccessor = new ElibUDataAccessor();
        Formats actFormats = elibUDataAccessor
                .getFormats(contentProviderConsumer, DevelopmentData.ELIBU_RECORD_ID, Locale.ENGLISH.getLanguage());
        assertNotNull(actFormats);
        List<Format> actFormatList = actFormats.asList();
        assertEquals(1, actFormatList.size());
    }
}
