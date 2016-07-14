package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.record.format.*;
import com.axiell.ehub.provider.record.issue.Issue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public abstract class ContentProviderDataAccessorTestFixture<A extends IContentProviderDataAccessor> {
    protected static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    protected static final String RECORD_ID = "1";
    protected static final String FORMAT_ID = FormatBuilder.FORMAT_ID;
    protected static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    protected static final long CONTENT_PROVIDER_CONSUMER_ID = 1L;
    protected static final long EHUB_CONSUMER_ID = 1L;
    protected static final String CONTENT_HREF = ContentLinkBuilder.HREF;
    protected static final String LANGUAGE = "sv";
    protected static final String PATRON_ID = "patronId";
    protected static final String CARD = "card";
    protected static final String PIN = "pin";
    private static final Date EXPIRATION_DATE = new Date();
    private static final int ERROR_STATUS = 500;

    protected A underTest;

    @Mock
    protected EhubConsumer ehubConsumer;
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
    protected WebApplicationException failure;
    @Mock
    protected Response response;
    @Mock
    protected IFormatFactory formatFactory;
    @Mock
    protected IExpirationDateFactory expirationDateFactory;
    @Mock
    protected CommandData commandData;
    @Mock
    protected Patron patron;

    protected Format format = FormatBuilder.downloadableFormat();
    protected ContentProviderLoan actualLoan;
    protected ContentLink actualContentLink;
    @Mock
    private PendingLoan pendingLoan;

    protected List<Format> actualFormats;

    @Before
    public void fixtureSetUp() {
        given(ehubConsumer.getId()).willReturn(EHUB_CONSUMER_ID);
        given(contentProviderConsumer.getId()).willReturn(CONTENT_PROVIDER_CONSUMER_ID);
        given(contentProviderConsumer.getEhubConsumer()).willReturn(ehubConsumer);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(getContentProviderName());
    }

    protected void givenContentProviderConsumerInCommandData() {
        given(commandData.getContentProviderConsumer()).willReturn(contentProviderConsumer);
    }


    protected void givenPatronInCommandData() {
        given(commandData.getPatron()).willReturn(patron);
    }

    protected void givenPatronIdInPatron() {
        given(patron.hasId()).willReturn(true);
        given(patron.getId()).willReturn(PATRON_ID);
    }

    protected void givenLibraryCardInPatron() {
        givenLibraryCardInPatron(CARD);
    }

    protected void givenLibraryCardInPatron(final String libraryCard) {
        given(patron.hasLibraryCard()).willReturn(true);
        given(patron.getLibraryCard()).willReturn(libraryCard);
    }

    protected void givenPinInPatron() {
        given(patron.getPin()).willReturn(PIN);
    }

    protected void givenContentProviderRecordIdInCommandData() {
        given(commandData.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    protected void givenContentProviderAliasInCommandData() {
        given(commandData.getContentProviderAlias()).willReturn(getContentProviderName());
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

    protected void givenContentProviderLoanIdFromLoanMetadata() {
        given(loanMetadata.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
    }

    protected void givenTextBundle() {
        givenFormatDecorationFromContentProvider();
        given(formatDecoration.getTextBundle(LANGUAGE)).willReturn(textBundle);
    }

    protected void givenContentProviderFormatIdFromFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    protected void givenFormatDecorationFromContentProvider() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    protected void givenDownloadableContentDisposition() {
        given(formatDecoration.getContentDisposition()).willReturn(ContentDisposition.DOWNLOADABLE);
    }

    protected void givenFormatDecorationFromContentProviderLoanMetadata() {
        given(loanMetadata.getFirstFormatDecoration()).willReturn(formatDecoration);
    }

    protected void givenFormatDecorationInCommandData() {
        given(commandData.getFormatDecoration()).willReturn(formatDecoration);
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

    protected void givenFormatFromFormatFactory() {
        given(formatFactory.create(any(ContentProvider.class), anyString(), anyString())).willReturn(format);
    }

    protected void thenActualLoanContainsContentLinkHref() {
        Assert.assertNotNull(actualLoan);
        actualContentLink = actualLoan.content().getContentLinks().getContentLinks().get(0);
        thenActualContentLinkContainsHref();
    }

    protected void thenActualContentLinkContainsHref() {
        Assert.assertEquals(CONTENT_HREF, actualContentLink.href());
    }

    protected void thenActualFormatEqualsExpected() {
        Assert.assertFalse(actualFormats.isEmpty());
        Format actualFormat = actualFormats.iterator().next();
        assertThat(actualFormat.toDTO(), FormatDTOMatcher.matchesExpectedFormatDTO(format.toDTO()));
    }

    private void thenFormatsNotNull() {
        Assert.assertNotNull(actualFormats);
    }

    protected void thenFormatSetContainsOneFormat() {
        Assert.assertTrue(actualFormats.size() == 1);
    }

    protected void thenFormatSetIsEmpty() {
        thenFormatsNotNull();
        assertTrue(actualFormats.isEmpty());
    }

    protected void whenGetIssues() {
        List<Issue> issues = underTest.getIssues(commandData);
        actualFormats = issues.get(0).getFormats();
    }

    protected void thenActualLoanHasExpirationDateCreatedByExpirationDateFactory() {
        assertEquals(EXPIRATION_DATE, actualLoan.expirationDate());
    }

    protected void thenSetContentProviderLoanMetadataInCommandDataHasBeenInvoked() {
        verify(commandData).setContentProviderLoanMetadata(any(ContentProviderLoanMetadata.class));
    }

    protected abstract String getContentProviderName();
}
