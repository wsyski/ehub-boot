package com.axiell.ehub.provider.publit;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.Formats;

public class PublitDataAccessorTest {
    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    private ContentProviderConsumer contentProviderConsumer = mockery.mock(ContentProviderConsumer.class);
    private ContentProvider contentProvider;
    private PublitDataAccessor publitDataAccessor;

    @Before
    public void setUp() throws Exception {
        publitDataAccessor = new PublitDataAccessor();
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, DevelopmentData.PUBLIT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL, DevelopmentData.PUBLIT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL, DevelopmentData.PUBLIT_URL);
        contentProvider = new ContentProvider(ContentProviderName.PUBLIT, contentProviderProperties);

        Map<String, FormatDecoration> formatDecoration = new HashMap<>();
        FormatDecoration formatDecoration0 = new FormatDecoration(contentProvider, DevelopmentData.PUBLIT_FORMAT_0_ID,
                ContentDisposition.DOWNLOADABLE, DevelopmentData.PUBLIT_PLAYER_WIDTH,
                DevelopmentData.PUBLIT_PLAYER_HEIGHT);
        formatDecoration.put(DevelopmentData.PUBLIT_FORMAT_0_ID, formatDecoration0);

        contentProvider.setFormatDecorations(formatDecoration);

        mockery.checking(new Expectations() {
            {
                allowing(contentProviderConsumer).getProperty(
                        with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_USERNAME)));
                will(returnValue(DevelopmentData.PUBLIT_USERNAME));
                allowing(contentProviderConsumer).getProperty(
                        with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.PUBLIT_PASSWORD)));
                will(returnValue(DevelopmentData.PUBLIT_PASSWORD));
                allowing(contentProviderConsumer).getContentProvider();
                will(returnValue(contentProvider));
            }
        });
    }

    @Test
    public void testGetFormats() {
        Formats actFormats = publitDataAccessor.getFormats(contentProviderConsumer,
                DevelopmentData.PUBLIT_RECORD_0_ID, Locale.ENGLISH.getLanguage());
        Assert.assertEquals(1, actFormats.getFormats().size());
        Format actFormat = actFormats.getFormats().iterator().next();
        Assert.assertEquals(DevelopmentData.PUBLIT_FORMAT_0_ID, actFormat.getId());
        Assert.assertEquals(DevelopmentData.PUBLIT_FORMAT_0_NAME, actFormat.getName());
        Assert.assertNull(actFormat.getDescription());
        Assert.assertNull(actFormat.getIconUrl());
    }

    @Test
    public void testCreateLoanAndGetContent() {
        PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID,
                ContentProviderName.PUBLIT.toString(), DevelopmentData.PUBLIT_RECORD_0_ID,
                DevelopmentData.PUBLIT_FORMAT_0_ID);

        ContentProviderLoan contentProviderLoan = publitDataAccessor.createLoan(contentProviderConsumer,
                DevelopmentData.PUBLIT_LIBRARY_CARD, DevelopmentData.PUBLIT_LIBRARY_CARD_PIN, pendingLoan);
        Assert.assertNotNull(contentProviderLoan);

        FormatDecoration formatDecoration = new FormatDecoration(contentProvider, DevelopmentData.PUBLIT_FORMAT_0_ID,
                ContentDisposition.DOWNLOADABLE, DevelopmentData.PUBLIT_PLAYER_WIDTH,
                DevelopmentData.PUBLIT_PLAYER_HEIGHT);
        ContentProviderLoanMetadata contentProviderLoanMetadata = new ContentProviderLoanMetadata(
                contentProviderLoan.getId(), contentProvider, new Date(), formatDecoration);

        IContent content = publitDataAccessor.getContent(contentProviderConsumer, DevelopmentData.PUBLIT_LIBRARY_CARD,
                DevelopmentData.PUBLIT_LIBRARY_CARD_PIN, contentProviderLoanMetadata);
        Assert.assertNotNull(content);
    }
}
