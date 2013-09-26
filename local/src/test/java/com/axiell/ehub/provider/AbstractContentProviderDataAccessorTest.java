package com.axiell.ehub.provider;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractContentProviderDataAccessorTest {
    protected static final String DOWNLOAD_URL = "url";
    protected static final String LANGUAGE = "sv";
    protected static final String CARD = "card";
    protected static final String PIN = "pin";
    protected static final String EHUB_FORMAT_NAME = "ehubFormatName";
    protected static final String EHUB_FORMAT_DESCRIPTION = "ehubFormatDescription";

    @Mock
    protected PendingLoan pendingLoan;
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;
    @Mock
    protected FormatDecoration formatDecoration;
    @Mock
    protected FormatTextBundle textBundle;
    @Mock
    protected ContentProviderLoanMetadata loanMetadata;
    protected Formats actualFormats;
    protected ContentProviderLoan actualLoan;
    protected IContent actualContent;

    protected void givenTextBundle() {
        givenContentProvider();
        givenFormatDecorationFromContentProvider();
        given(formatDecoration.getTextBundle(LANGUAGE)).willReturn(textBundle);
    }

    protected void givenContentProvider() {
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
    }

    protected void givenFormatDecorationFromContentProvider() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    protected void givenEhubFormatNameAndDescription() {
        given(textBundle.getName()).willReturn(EHUB_FORMAT_NAME);
        given(textBundle.getDescription()).willReturn(EHUB_FORMAT_DESCRIPTION);
    }

    protected void givenContentDisposition() {
        given(formatDecoration.getContentDisposition()).willReturn(ContentDisposition.DOWNLOADABLE);
    }

    protected void givenFormatDecorationFromContentProviderLoanMetadata() {
        given(loanMetadata.getFormatDecoration()).willReturn(formatDecoration);
    }

    protected void thenActualLoanContainsDownloadUrl() {
        Assert.assertNotNull(actualLoan);
        IContent content = actualLoan.getContent();
        DownloadableContent downloadableContent = (DownloadableContent) content;
        Assert.assertEquals(DOWNLOAD_URL, downloadableContent.getUrl());
    }

    protected void thenActualContentContainsDownloadUrl() {
        DownloadableContent downloadableContent = (DownloadableContent) actualContent;
        Assert.assertEquals(DOWNLOAD_URL, downloadableContent.getUrl());
    }

    protected void thenFormatHasEhubFormatNameAndDescription() {
        Assert.assertFalse(actualFormats.getFormats().isEmpty());
        Format actualFormat = actualFormats.getFormats().iterator().next();
        Assert.assertEquals(EHUB_FORMAT_NAME, actualFormat.getName());
        Assert.assertEquals(EHUB_FORMAT_DESCRIPTION, actualFormat.getDescription());
    }
}
