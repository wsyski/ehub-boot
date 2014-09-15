package com.axiell.ehub.provider;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.provider.record.format.*;
import junit.framework.Assert;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.Set;

import static com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition.DOWNLOADABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractContentProviderDataAccessorTest {
    protected static final String RECORD_ID = "1";
    protected static final String FORMAT_ID = "1";
    protected static final String DOWNLOAD_URL = "url";
    protected static final String LANGUAGE = "sv";
    protected static final String CARD = "card";
    protected static final String PIN = "pin";
    protected static final String EHUB_FORMAT_NAME = "ehubFormatName";
    protected static final String EHUB_FORMAT_DESCRIPTION = "ehubFormatDescription";
    private static final Date EXPIRATION_DATE = new Date();
    private static final int ERROR_STATUS = 500;

    @Mock
    protected IContentFactory contentFactory;
    @Mock
    protected DownloadableContent downloadableContent;
//    @Mock
//    protected PendingLoan pendingLoan;
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
    @Mock
    protected ClientResponseFailure failure;
    @Mock
    protected ClientResponse<?> response;
    @Mock
    protected IFormatFactory formatFactory;
    @Mock
    protected IExpirationDateFactory expirationDateFactory;
    @Mock
    private PendingLoan pendingLoan;
    @Mock
    protected CommandData commandData;
    protected Formats actualFormats;
    protected ContentProviderLoan actualLoan;
    protected IContent actualContent;

    protected void givenContentProviderConsumerInCommandData() {
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
    }

    protected void givenLibraryCardInCommandData() {
        given(commandData.getLibraryCard()).willReturn(CARD);
    }

    protected void givenPinInCommandData() {
        given(commandData.getPin()).willReturn(PIN);
    }

    protected void givenContentProviderRecordIdInCommandData() {
        given(commandData.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    protected void givenContentProviderFormatIdInCommandData() {
        given(commandData.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    protected void givenLanguageInCommandData() {
        given(commandData.getLanguage()).willReturn(LANGUAGE);
    }

    protected void givenContentProviderLoanMetadataInCommandData() {
        given(commandData.getContentProviderLoanMetadata()).willReturn(loanMetadata);
    }

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

    protected void givenDownloadableContentDisposition() {
        given(formatDecoration.getContentDisposition()).willReturn(DOWNLOADABLE);
    }

    protected void givenFormatDecorationFromContentProviderLoanMetadata() {
        given(loanMetadata.getFormatDecoration()).willReturn(formatDecoration);
    }

    protected void givenExpirationDate() {
        given(expirationDateFactory.createExpirationDate(contentProvider)).willReturn(EXPIRATION_DATE);
    }

    protected void givenClientResponse() {
        given(failure.getResponse()).willReturn(response);
    }

    protected void givenClientResponseStatus() {
        given(response.getStatus()).willReturn(ERROR_STATUS);
    }

    protected void givenCreatedDownloadableContent() {
        given(downloadableContent.getUrl()).willReturn(DOWNLOAD_URL);
        given(contentFactory.create(DOWNLOAD_URL, formatDecoration)).willReturn(downloadableContent);
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

    protected Set<Format> thenFormatSetIsNotNull() {
        Assert.assertNotNull(actualFormats);
        Set<Format> formatSet = actualFormats.getFormats();
        Assert.assertNotNull(formatSet);
        return formatSet;
    }

    protected void thenFormatSetContainsOneFormat() {
        Set<Format> formatSet = thenFormatSetIsNotNull();
        Assert.assertTrue(formatSet.size() == 1);
    }

    protected void thenFormatSetIsEmpty() {
        Set<Format> formatSet = thenFormatSetIsNotNull();
        assertTrue(formatSet.isEmpty());
    }

    protected void thenActualLoanHasExpirationDateCreatedByExpirationDateFactory() {
        assertEquals(EXPIRATION_DATE, actualLoan.getExpirationDate());
    }
//

}
