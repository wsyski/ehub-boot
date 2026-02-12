package com.axiell.ehub.local.provider;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.CheckoutMetadataBuilder;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.checkout.ContentLink;
import com.axiell.ehub.common.checkout.ContentLinkBuilder;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.ContentDisposition;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatBuilder;
import com.axiell.ehub.common.provider.record.format.FormatDTOMatcher;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.common.provider.record.issue.IssueBuilder;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.loan.PendingLoan;
import com.axiell.ehub.local.provider.record.format.IFormatFactory;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class ContentProviderDataAccessorTestFixture<A extends IContentProviderDataAccessor> {
    protected static final Format DOWNLOADABLE_FORMAT = FormatBuilder.downloadableFormat();
    protected static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";
    protected static final String RECORD_ID = "recordId";
    protected static final String ISSUE_ID = IssueBuilder.ISSUE_ID;
    protected static final String FORMAT_ID = FormatBuilder.FORMAT_ID;
    protected static final String CONTENT_PROVIDER_LOAN_ID = "contentProviderLoanId";
    protected static final long CONTENT_PROVIDER_CONSUMER_ID = 1L;
    protected static final long EHUB_CONSUMER_ID = 1L;
    protected static final String CONTENT_HREF = ContentLinkBuilder.HREF;
    protected static final String LANGUAGE = Locale.ENGLISH.getLanguage();
    protected static final String PATRON_ID = "patronId";
    protected static final String CARD = "card";
    protected static final String PIN = "pin";
    protected static final Date EXPIRATION_DATE = CheckoutMetadataBuilder.EXPIRATION_DATE;
    protected static final int ERROR_STATUS = 500;

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
    @Mock
    private PendingLoan pendingLoan;

    protected ContentProviderLoan actualLoan;
    protected Content actualContent;
    protected List<Issue> actualIssues;
    protected A underTest;

    @BeforeEach
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
        given(patron.hasPin()).willReturn(true);
        given(patron.getPin()).willReturn(PIN);
    }

    protected void givenContentProviderRecordIdInCommandData() {
        given(commandData.getContentProviderRecordId()).willReturn(RECORD_ID);
    }

    protected void givenContentProviderIssueIdInCommandData() {
        given(commandData.getIssueId()).willReturn(ISSUE_ID);
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

    protected void givenContentProviderLoanIdInLoanMetadata() {
        given(loanMetadata.getId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
    }

    protected void givenContentProviderIssueIdInLoanMetadata() {
        given(loanMetadata.getIssueId()).willReturn(ISSUE_ID);
    }

    protected void givenTextBundle() {
        givenFormatDecorationInContentProvider();
        given(formatDecoration.getTextBundle(LANGUAGE)).willReturn(textBundle);
    }

    protected void givenContentProviderFormatIdInFormatDecoration() {
        given(formatDecoration.getContentProviderFormatId()).willReturn(FORMAT_ID);
    }

    protected void givenFormatDecorationInContentProvider() {
        given(contentProvider.getFormatDecoration(any(String.class))).willReturn(formatDecoration);
    }

    protected void givenDownloadableContentDisposition() {
        given(formatDecoration.getContentDisposition()).willReturn(ContentDisposition.DOWNLOADABLE);
    }

    protected void givenFormatDecorationInContentProviderLoanMetadata() {
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

    protected void givenFormatInFormatFactory() {
        given(formatFactory.create(any(ContentProvider.class), anyString(), anyString())).willReturn(DOWNLOADABLE_FORMAT);
    }

    protected void thenActualLoanContainsContentLinkHref() {
        Assertions.assertNotNull(actualLoan);
        actualContent = actualLoan.content();
        thenActualContentLinkContainsHref();
    }

    protected void thenActualContentLinkContainsHref() {
        Assertions.assertEquals(CONTENT_HREF, getActualContentLink().href());
    }

    protected void thenActualFormatEqualsExpected() {
        List<Format> formats = getActualFormats();
        Assertions.assertFalse(formats.isEmpty());
        Format actualFormat = formats.iterator().next();
        assertThat(actualFormat.toDTO(), FormatDTOMatcher.matchesExpectedFormatDTO(DOWNLOADABLE_FORMAT.toDTO()));
    }

    private void thenFormatsNotNull() {
        Assertions.assertNotNull(getActualFormats());
    }

    protected void thenActualFormatsContainsOneFormat() {
        Assertions.assertTrue(getActualFormats().size() == 1);
    }

    protected void thenFormatsEmpty() {
        thenFormatsNotNull();
        Assertions.assertTrue(getActualFormats().isEmpty());
    }

    protected void whenGetIssues() {
        actualIssues = underTest.getIssues(commandData);
    }

    protected void whenCreateLoan() {
        actualLoan = underTest.createLoan(commandData);
    }

    public void whenGetContent() {
        actualContent = underTest.getContent(commandData);
    }

    protected List<Format> getActualFormats() {
        assertThat(actualIssues, Matchers.notNullValue());
        assertThat(actualIssues.size(), Matchers.greaterThan(0));
        return actualIssues.iterator().next().getFormats();
    }

    protected ContentLink getActualContentLink() {
        assertThat(actualContent, Matchers.notNullValue());
        List<ContentLink> contentLinks = actualContent.getContentLinks().getContentLinks();
        assertThat(contentLinks, Matchers.notNullValue());
        assertThat(contentLinks.size(), Matchers.greaterThan(0));
        return contentLinks.iterator().next();
    }

    protected void thenActualLoanHasExpirationDateCreatedByExpirationDateFactory() {
        Assertions.assertEquals(EXPIRATION_DATE, actualLoan.expirationDate());
    }

    protected abstract String getContentProviderName();
}
