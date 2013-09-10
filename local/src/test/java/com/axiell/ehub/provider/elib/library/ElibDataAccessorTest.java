/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import se.elib.library.orderlist.Response.Data.Orderitem;

import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * Elib Data Accessor Test
 */
public class ElibDataAccessorTest {
    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    private ContentProviderConsumer contentProviderConsumer = mockery.mock(ContentProviderConsumer.class);
    private ContentProvider contentProvider;
    private ElibDataAccessor elibDataAccessor;

    @Before
    public void setUp() throws Exception {
        elibDataAccessor = new ElibDataAccessor();
        Map<ContentProvider.ContentProviderPropertyKey, String> contentProviderProperties = new HashMap<>();
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, DevelopmentData.ELIB_PRODUCT_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL, DevelopmentData.ELIB_CREATE_LOAN_URL);
        contentProviderProperties.put(ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL, DevelopmentData.ELIB_ORDER_LIST_URL);
        contentProvider = new ContentProvider(ContentProviderName.ELIB, contentProviderProperties);

        Map<String, FormatDecoration> formatDecorations = new HashMap<>();
        FormatDecoration
                formatDecoration0 =
                new FormatDecoration(contentProvider, DevelopmentData.ELIB_FORMAT_0_ID, ContentDisposition.DOWNLOADABLE, DevelopmentData.ELIB_PLAYER_WIDTH,
                        DevelopmentData.ELIB_PLAYER_HEIGHT);
        formatDecorations.put(DevelopmentData.ELIB_FORMAT_0_ID, formatDecoration0);

        contentProvider.setFormatDecorations(formatDecorations);

        mockery.checking(new Expectations() {
            {
                allowing(contentProviderConsumer).getProperty(with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID)));
                will(returnValue(DevelopmentData.ELIB_SERVICE_ID));
                allowing(contentProviderConsumer).getProperty(with(equal(ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY)));
                will(returnValue(DevelopmentData.ELIB_SERVICE_KEY));
                allowing(contentProviderConsumer).getContentProvider();
                will(returnValue(contentProvider));
            }
        });
    }
    
    @Test
    public void testCreateLoan() {
	// TODO:
	boolean online = true;
	if (online) {
	    PendingLoan pendingLoan = new PendingLoan(DevelopmentData.LMS_RECORD_ID, ContentProviderName.ELIB.toString(), DevelopmentData.ELIB_RECORD_0_ID,
	                DevelopmentData.ELIB_FORMAT_0_ID);
	    
	    ContentProviderLoan contentProviderLoan =
	                elibDataAccessor.createLoan(contentProviderConsumer, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN, pendingLoan);
	    Assert.assertNotNull(contentProviderLoan);    
	    IContent content = contentProviderLoan.getContent();
	    Assert.assertNotNull(content);
	    assertUrlNotNull(content);
	}
    }
    
    private void assertUrlNotNull(IContent content) {
	String url;
        
        if (content instanceof DownloadableContent) {
            DownloadableContent downloadableContent = (DownloadableContent) content;
            url = downloadableContent.getUrl();
        } else {
            StreamingContent streamingContent = (StreamingContent) content;
            url = streamingContent.getUrl();
        }
        
        Assert.assertNotNull(url);
    }
    
    @Test
    public void testGetContent() {
        FormatDecoration formatDecoration =
                new FormatDecoration(contentProvider, DevelopmentData.ELIB_FORMAT_0_ID, ContentDisposition.DOWNLOADABLE, DevelopmentData.ELIB_PLAYER_WIDTH,
                        DevelopmentData.ELIB_PLAYER_HEIGHT);
        ContentProviderLoanMetadata contentProviderLoanMetadata =
                new ContentProviderLoanMetadata(DevelopmentData.ELIB_RETAIL_ORDER_NUMBER, contentProvider, new Date(), formatDecoration);
        
        IContent content = elibDataAccessor
                .getContent(contentProviderConsumer, DevelopmentData.ELIB_LIBRARY_CARD, DevelopmentData.ELIB_LIBRARY_CARD_PIN, contentProviderLoanMetadata);
        Assert.assertNotNull(content);
        assertUrlNotNull(content);
    }

    
    @Test
    public void testGetOrderItems() {
        List<Orderitem> orderItems = elibDataAccessor.getOrderItems(contentProviderConsumer, DevelopmentData.ELIB_LIBRARY_CARD);
        Assert.assertNotNull(orderItems);
    }
   
    @Test
    public void testGetFormats() {
        Formats actFormats =
                elibDataAccessor.getFormats(contentProviderConsumer, DevelopmentData.ELIB_RECORD_0_ID, Locale.ENGLISH.getLanguage());
        Assert.assertEquals(1, actFormats.getFormats().size());
        Format actFormat = actFormats.getFormats().iterator().next();
        Assert.assertEquals(DevelopmentData.ELIB_FORMAT_0_ID, actFormat.getId());
        Assert.assertEquals(DevelopmentData.ELIB_FORMAT_0_NAME, actFormat.getName());
        Assert.assertNull(actFormat.getDescription());
        Assert.assertNull(actFormat.getIconUrl());
    }
}
