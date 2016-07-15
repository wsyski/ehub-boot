package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderDataAccessorTestFixture;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.record.issue.IssueBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class ZinioDataAccessorTest extends ContentProviderDataAccessorTestFixture<ZinioDataAccessor> {
    private final static String LOGIN_URL = "loginUrl";
    private final static String CONTENT_URL = ContentLinkBuilder.HREF;
    @Mock
    private IZinioFacade zinioFacade;
    @Mock
    private InternalServerErrorException internalServerErrorException;
    @Mock
    private IEhubExceptionFactory ehubExceptionFactory;
    @Mock
    private IExpirationDateFactory expirationDateFactory;
    private List<IssueDTO> issues = Collections.singletonList(IssueDTO.toIssueDTO(IssueBuilder.periodicalIssue()));

    @Before
    public void setUp() {
        underTest = new ZinioDataAccessor();
        ReflectionTestUtils.setField(underTest, "zinioFacade", zinioFacade);
        ReflectionTestUtils.setField(underTest, "formatFactory", formatFactory);
        ReflectionTestUtils.setField(underTest, "expirationDateFactory", expirationDateFactory);
    }

    @Test
    public void getIssues() {
        givenLanguageInCommandData();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderAliasInCommandData();
        givenPatronInCommandData();
        givenZinioFacadeReturnsFormats();
        givenFormatDecorationInContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatInFormatFactory();
        whenGetIssues();
        thenActualFormatsContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    @Test
    public void createLoan() {
        givenLanguageInCommandData();
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationInContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenContentProviderIssueIdInCommandData();
        givenPatronInCommandData();
        givenLoginUrl();
        givenContentUrl();
        givenExpirationDateFactory();
        givenCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenLanguageInCommandData();
        givenContentProviderConsumerInCommandData();
        givenContentProviderLoanMetadataInCommandData();
        givenContentProviderIssueIdInCommandData();
        givenFormatDecorationInCommandData();
        givenPatronInCommandData();
        givenContentProviderIssueIdInLoanMetadata();
        givenLoginUrl();
        givenContentUrl();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }

    private void givenZinioFacadeReturnsFormats() {
        given(zinioFacade.getIssues(contentProviderConsumer, RECORD_ID, LANGUAGE)).willReturn(issues);
    }


    private void givenLoginUrl() {
        given(zinioFacade.login(any(ContentProviderConsumer.class), any(Patron.class), eq(LANGUAGE))).willReturn(LOGIN_URL);

    }

    private void givenContentUrl() {
        given(zinioFacade.getContentUrl(LOGIN_URL, ISSUE_ID)).willReturn(CONTENT_URL);
    }

    private void givenExpirationDateFactory() {
        given(expirationDateFactory.createExpirationDate(contentProvider)).willReturn(EXPIRATION_DATE);
    }

    public void givenCheckout() {
        zinioFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), eq(LANGUAGE));
    }


    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_BORROWBOX;
    }
}
