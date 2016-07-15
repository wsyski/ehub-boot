package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.error.IEhubExceptionFactory;
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

public class ZinioDataAccessorTest extends ContentProviderDataAccessorTestFixture<ZinioDataAccessor> {

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
        givenFormatDecorationFromContentProvider();
        givenContentProviderConsumerInCommandData();
        givenFormatFromFormatFactory();
        whenGetIssues();
        thenActualFormatsContainsOneFormat();
        thenActualFormatEqualsExpected();
    }

    private void givenZinioFacadeReturnsFormats() {
        given(zinioFacade.getIssues(contentProviderConsumer, RECORD_ID, LANGUAGE)).willReturn(issues);
        /*
        given(issues.getFormats()).willReturn(Collections.singletonList(format));
        given(format.getFormatId()).willReturn(FORMAT_ID);
        */
    }

    /*
    @Test
    public void createLoan() {
        givenContentProviderConsumerInCommandData();
        givenFormatDecorationFromContentProvider();
        givenContentProviderRecordIdInCommandData();
        givenCompleteCheckout();
        givenCheckout();
        whenCreateLoan();
        thenActualLoanContainsContentLinkHref();
    }

    @Test
    public void getContent() {
        givenGetCheckout();
        givenCompleteCheckout();
        givenContentProviderLoanMetadataInCommandData();
        givenFormatDecorationInCommandData();
        whenGetContent();
        thenActualContentLinkContainsHref();
    }



    public void givenCheckout() {
        given(zinioFacade.checkout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class), any(String.class)))
                .willReturn(checkout);
    }

    public void givenGetCheckout() {
        given(zinioFacade.getCheckout(any(ContentProviderConsumer.class), any(Patron.class), any(String.class), any(String.class))).willReturn(checkout);
    }

    public void givenCompleteCheckout() {
        given(checkout.getExpirationDate()).willReturn(new Date());
        given(checkout.getLoanId()).willReturn(CONTENT_PROVIDER_LOAN_ID);
        given(checkout.getContentUrl()).willReturn(CONTENT_HREF);
    }
      */


    @Override
    protected String getContentProviderName() {
        return ContentProvider.CONTENT_PROVIDER_BORROWBOX;
    }
}
