package com.axiell.ehub.loan;

import com.axiell.ehub.*;
import com.axiell.ehub.checkout.*;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import com.axiell.ehub.error.EhubExceptionMatcher;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis;
import com.axiell.ehub.lms.palma.CheckoutTestAnalysis.Result;
import com.axiell.ehub.lms.palma.IPalmaDataAccessor;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IContentProviderDataAccessorFacade;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.security.AuthInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static com.axiell.ehub.checkout.CheckoutMetadataDTOMatcher.matchesExpectedCheckoutMetadataDTO;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class LoanBusinessControllerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String CONTENT_PROVIDER_TEST_EP = "TEST_EP";

    private static final Long READY_LOAN_ID = 0L;
    private static final String LANGUAGE = "en";
    private static final String LMS_LOAN_ID = "lmsLoanId";
    private static final String NEW_CONTENT_PROVIDER_FORMAT_ID = "newContentProviderFormatId";

    private ILoanBusinessController underTest;

    @Mock
    private IConsumerBusinessController consumerBusinessController;
    @Mock
    private IPalmaDataAccessor palmaDataAccessor;
    @Mock
    private IEhubLoanRepositoryFacade ehubLoanRepositoryFacade;
    @Mock
    private IContentProviderDataAccessorFacade contentProviderDataAccessorFacade;
    @Mock
    private EhubConsumer ehubConsumer;
    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ContentProviderConsumer contentProviderConsumer;
    @Mock
    private FormatDecoration formatDecoration;
    @Mock
    private FormatDecoration newFormatDecoration;
    @Mock
    private LmsLoan lmsLoan;
    @Mock
    private ContentLink contentLink;
    @Mock
    private Content content;
    @Mock
    private ContentProviderLoan contentProviderLoan;
    @Mock
    private ContentProviderLoanMetadata contentProviderLoanMetadata;
    @Mock
    private EhubLoan ehubLoan;
    @Mock
    private ICheckoutMetadataFactory checkoutMetadataFactory;
    @Mock
    private ICheckoutFactory checkoutFactory;

    private AuthInfo authInfo;
    private Fields fields = FieldsBuilder.defaultFields();
    private CheckoutMetadata checkoutMetadata;
    private Checkout actualCheckout;
    private CheckoutsSearchResult actualSearchResult;

    @Before
    public void setUpCommonArguments() throws EhubException {
        authInfo = new AuthInfo.Builder(0L, "secret").libraryCard("libraryCard").pin("pin").build();
    }

    @Before
    public void setUpLoanBusinessController() {
        checkoutMetadata = CheckoutMetadataBuilder.checkoutMetadataWithStreamingFormat();
        given(contentProviderLoanMetadata.getFirstFormatDecoration()).willReturn(formatDecoration);
        given(checkoutMetadataFactory.create(any(EhubLoan.class), any(FormatDecoration.class), anyString(), anyBoolean())).willReturn(checkoutMetadata);
        given(contentProviderConsumer.getContentProvider()).willReturn(contentProvider);
        setUpEhubLoan();
        underTest = new LoanBusinessController();
        ReflectionTestUtils.setField(underTest, "consumerBusinessController", consumerBusinessController);
        ReflectionTestUtils.setField(underTest, "palmaDataAccessor", palmaDataAccessor);
        ReflectionTestUtils.setField(underTest, "ehubLoanRepositoryFacade", ehubLoanRepositoryFacade);
        ReflectionTestUtils.setField(underTest, "contentProviderDataAccessorFacade", contentProviderDataAccessorFacade);
        ReflectionTestUtils.setField(underTest, "checkoutMetadataFactory", checkoutMetadataFactory);
        ReflectionTestUtils.setField(underTest, "checkoutFactory", checkoutFactory);
    }

    @Before
    public void setUpEhubLoan() {
        given(ehubLoan.getContentProviderLoanMetadata()).willReturn(contentProviderLoanMetadata);
        given(ehubLoan.getLmsLoan()).willReturn(lmsLoan);
    }

    @Before
    public void setUpEhubConsumer() {
        given(consumerBusinessController.getEhubConsumer(any(Long.class))).willReturn(ehubConsumer);
    }

    @Before
    public void setUpContentProviderNameFromExistingLoan() {
        given(contentProviderLoanMetadata.getContentProvider()).willReturn(contentProvider);
        given(contentProvider.getName()).willReturn(CONTENT_PROVIDER_TEST_EP);
    }

    @Before
    public void setUpContentProviderLoan() {
        given(contentProviderDataAccessorFacade.createLoan(any(EhubConsumer.class), any(Patron.class), any(PendingLoan.class), any(String.class))).willReturn(
                contentProviderLoan);
    }
    @Before
    public void setUpGetContentProviderConsumer() {
        given(contentProviderDataAccessorFacade.getContentProviderConsumer(any(EhubConsumer.class), any(String.class))).willReturn(contentProviderConsumer);
    }

    @Before
    public void setUpSavedEhubLoan() {
        given(ehubLoanRepositoryFacade.saveEhubLoan(any(EhubConsumer.class), any(LmsLoan.class), any(ContentProviderLoan.class))).willReturn(ehubLoan);
    }


    @Test
    public void createNewLoan() throws EhubException {
        givenNewLoanAsPreCheckoutAnalysisResult();
        given(contentProviderLoan.content()).willReturn(content);
        whenCreateLoan();
        thenNewEhubLoanIsSavedInTheEhubDatabase();
    }

    @Test
    public void search_found() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        whenSearch();
        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        thenContentIsNotRetrievedFromContentProvider(inOrder);
        thenActualSearchResultEqualsExpected();
    }

    @Test
    public void activeLoan_firstFormat() {
        givenActiveLoanAsPreCheckoutAnalysisResult();
        givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        givenContentProviderFormatId(FieldsBuilder.CONTENT_PROVIDER_FORMAT_ID);
        whenCreateLoan();
        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        thenContentIsRetrievedFromContentProvider(inOrder);
    }


    @Test
    public void activeLoan_unsupportedLoanPerProduct() {
        givenActiveLoanAsPreCheckoutAnalysisResult();
        givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        givenContentProviderFormatId(NEW_CONTENT_PROVIDER_FORMAT_ID);
        givenExpectedUnsupportedLoanPerProduct();
        whenCreateLoan();
    }

    @Test
    public void getReadyLoanByReadyLoanId() {
        givenEhubLoanCanBeFoundInTheEhubDatabaseByReadyLoanLoanId();

        whenGetReadyLoanByReadLoanId();

        InOrder inOrder = thenEhubLoanIsFoundInTheEhubDatabaseByReadyLoanId();
        thenContentIsRetrievedFromContentProvider(inOrder);
    }

    @Test
    public void search_notFound() {
        givenEhubLoanCanNotBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId();
        whenSearch();
        thenSearchResultDoesNotContainLoanWithExpectedLmsLoanId();
    }

    private void givenContentProviderFormatId(final String contentProviderFormatId) {
        given(formatDecoration.getContentProviderFormatId()).willReturn(contentProviderFormatId);
    }

    private void givenExpectedUnsupportedLoanPerProduct() {
        final ErrorCauseArgument argument = new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProvider.getName());
        expectedException.expect(new EhubExceptionMatcher(InternalServerErrorException.class,ErrorCause.CONTENT_PROVIDER_UNSUPPORTED_LOAN_PER_PRODUCT, argument));
    }


    private ErrorCause getErrorCause(final NotFoundException e) {
        Assert.assertNotNull(e);
        EhubError ehubError = e.getEhubError();
        return ehubError.getCause();
    }

    private void givenNewLoanAsPreCheckoutAnalysisResult() {
        CheckoutTestAnalysis preCheckoutAnalysis = new CheckoutTestAnalysis(Result.NEW_LOAN, null);
        given(palmaDataAccessor.checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(Patron.class), any(boolean.class))).willReturn(
                preCheckoutAnalysis);
    }

    private void whenCreateLoan() {
        actualCheckout = underTest.checkout(authInfo, fields, LANGUAGE);
    }

    private void thenNewEhubLoanIsSavedInTheEhubDatabase() {
        InOrder inOrder =
                inOrder(consumerBusinessController, palmaDataAccessor, contentProviderDataAccessorFacade, palmaDataAccessor, ehubLoanRepositoryFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(palmaDataAccessor).checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(Patron.class), any(boolean.class));
        inOrder.verify(contentProviderDataAccessorFacade).createLoan(any(EhubConsumer.class), any(Patron.class), any(PendingLoan.class), any(String.class));
        inOrder.verify(palmaDataAccessor).checkout(any(EhubConsumer.class), any(PendingLoan.class), any(Date.class), any(Patron.class), any(boolean.class));
        inOrder.verify(ehubLoanRepositoryFacade).saveEhubLoan(any(EhubConsumer.class), any(LmsLoan.class), any(ContentProviderLoan.class));
    }

    private void givenActiveLoanAsPreCheckoutAnalysisResult() {
        CheckoutTestAnalysis preCheckoutAnalysis = new CheckoutTestAnalysis(Result.ACTIVE_LOAN, LMS_LOAN_ID);
        given(palmaDataAccessor.checkoutTest(any(EhubConsumer.class), any(PendingLoan.class), any(Patron.class), any(boolean.class))).willReturn(
                preCheckoutAnalysis);
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepositoryFacade.findEhubLoan(any(EhubConsumer.class), any(String.class))).willReturn(ehubLoan);
    }

    private InOrder thenEhubLoanIsFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId() {
        InOrder inOrder = inOrder(consumerBusinessController, ehubLoanRepositoryFacade, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(ehubLoanRepositoryFacade).findEhubLoan(any(EhubConsumer.class), (any(String.class)));
        return inOrder;
    }

    private void thenContentIsRetrievedFromContentProvider(InOrder inOrder) {
        inOrder.verify(contentProviderDataAccessorFacade)
                .getContent(any(EhubConsumer.class), any(EhubLoan.class), any(FormatDecoration.class), any(Patron.class), any(String.class));
    }

    private void givenEhubLoanCanBeFoundInTheEhubDatabaseByReadyLoanLoanId() {
        given(ehubLoanRepositoryFacade.findEhubLoan(anyLong())).willReturn(ehubLoan);
    }

    private void whenGetReadyLoanByReadLoanId() {
        actualCheckout = underTest.getCheckout(authInfo, READY_LOAN_ID, LANGUAGE);
    }

    private InOrder thenEhubLoanIsFoundInTheEhubDatabaseByReadyLoanId() {
        InOrder inOrder = inOrder(consumerBusinessController, ehubLoanRepositoryFacade, contentProviderDataAccessorFacade);
        inOrder.verify(consumerBusinessController).getEhubConsumer(any(AuthInfo.class));
        inOrder.verify(ehubLoanRepositoryFacade).findEhubLoan(anyLong());
        return inOrder;
    }

    private void whenSearch() {
        actualSearchResult = underTest.search(authInfo, LMS_LOAN_ID, LANGUAGE);
    }

    private void thenContentIsNotRetrievedFromContentProvider(InOrder inOrder) {
        inOrder.verify(contentProviderDataAccessorFacade, never())
                .getContent(any(EhubConsumer.class), any(EhubLoan.class), any(FormatDecoration.class), any(Patron.class), any(String.class));
    }

    private void thenActualSearchResultEqualsExpected() {
        CheckoutMetadata actualCheckoutMetadata = actualSearchResult.findCheckoutByLmsLoanId(LMS_LOAN_ID);
        assertThat(actualCheckoutMetadata.toDTO(), matchesExpectedCheckoutMetadataDTO(checkoutMetadata.toDTO()));
    }

    private void givenEhubLoanCanNotBeFoundInTheEhubDatabaseByEhubConsumerIdAndLmsLoanId() {
        given(ehubLoanRepositoryFacade.findEhubLoan(any(EhubConsumer.class), anyString())).willReturn(null);
    }

    private void thenSearchResultDoesNotContainLoanWithExpectedLmsLoanId() {
        try {
            actualSearchResult.findCheckoutByLmsLoanId(LMS_LOAN_ID);
            fail("A NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            assertNotNull(e);
        }
    }
}
